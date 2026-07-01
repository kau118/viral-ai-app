import React, { useState, useEffect } from "react";
import { motion } from "motion/react";
import { TrendingUp, Users, Zap, Clock, ChevronRight, Activity, Cpu, Search } from "lucide-react";
import { API_BASE } from "../constants";

export default function Dashboard() {
  const [loading, setLoading] = useState(true);
  const [aiStatus, setAiStatus] = useState("Initializing...");
  const [viralScore, setViralScore] = useState(0);
  const [backendStatus, setBackendStatus] = useState<"connecting" | "online" | "offline">("connecting");
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    let intervalId: any;

    // Check Backend Connection
    const checkConnection = async () => {
      console.log("Checking backend: " + API_BASE);
      try {
        const res = await fetch(`${API_BASE}/api/health`);
        if (res.ok) {
          console.log("Backend Online");
          setBackendStatus("online");
          setErrorMessage(null);
        } else {
          setBackendStatus("offline");
          setErrorMessage(`HTTP ${res.status}`);
        }
      } catch (e: any) {
        console.error("Backend offline or sleep");
        setBackendStatus("offline");
        setErrorMessage(e.message || "Network Error");
      }
    };

    checkConnection();

    // Retry every 8 seconds if not online (Render free tier wakes up slowly)
    intervalId = setInterval(() => {
      setBackendStatus(prev => {
        if (prev !== "online") {
          checkConnection();
          return "connecting";
        }
        return prev;
      });
    }, 8000);

    // Simulate real-time initialization
    const timer = setTimeout(() => {
      setLoading(false);
      setAiStatus("🟢 AI Active");
      animateScore(88);
    }, 1200);

    return () => {
      clearTimeout(timer);
      clearInterval(intervalId);
    };
  }, []);

  const animateScore = (target: number) => {
    let current = 0;
    const interval = setInterval(() => {
      if (current >= target) {
        clearInterval(interval);
      } else {
        current += 1;
        setViralScore(current);
      }
    }, 20);
  };

  return (
    <div className="space-y-8 animate-fade-in">
      {/* Welcome Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
        <div>
          <div className="flex items-center gap-2 mb-1">
             <span className="text-[10px] font-mono font-black text-secondary uppercase tracking-[0.3em]">Neural Command</span>
          </div>
          <h1 className="font-display text-5xl font-black tracking-tight text-white">
            Welcome, <span className="text-primary">Alex</span>
          </h1>
          <p className="text-white/40 text-sm mt-2 flex items-center gap-2">
            <Search size={14} className="text-secondary" />
            Global trend-engine is synchronizing...
          </p>
        </div>
        <div className="bg-[#0b1326] border border-white/10 rounded-[1.5rem] px-6 py-3 flex items-center gap-4 shadow-xl">
          <div className="flex flex-col items-end">
            <div className="flex items-center gap-2">
               <div className={`w-2 h-2 rounded-full ${backendStatus === 'online' ? 'bg-secondary animate-pulse' : backendStatus === 'connecting' ? 'bg-primary animate-bounce' : 'bg-red-500'}`} />
               <span className={`font-mono text-[10px] font-black tracking-widest uppercase ${backendStatus === 'online' ? 'text-secondary' : 'text-white/40'}`}>
                 {backendStatus === 'online' ? 'SYSTEM ONLINE' : backendStatus === 'connecting' ? 'SYNCING...' : 'SYSTEM OFFLINE'}
               </span>
            </div>
            {errorMessage && backendStatus === 'offline' && (
              <span className="text-[8px] font-mono text-red-400 mt-0.5">{errorMessage}</span>
            )}
            <span className="font-mono text-[8px] font-black text-white/20 tracking-[0.2em] uppercase mt-0.5">
              {aiStatus}
            </span>
          </div>
          <div className="relative">
             <Cpu size={20} className={backendStatus === 'online' ? "text-secondary" : "text-white/20"} />
          </div>
        </div>
      </div>

      {/* Bento Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">

        {/* Virality Score Card */}
        <div className="lg:col-span-5">
          <div className="glass-card rounded-[3rem] p-10 flex flex-col items-center justify-center text-center relative overflow-hidden h-full min-h-[450px]">
            <div className="absolute top-0 left-0 w-full h-full bg-gradient-to-br from-primary/10 via-transparent to-transparent pointer-events-none" />

            <h3 className="font-mono text-[11px] tracking-[0.4em] text-white/30 font-black uppercase mb-10">
              Aggregated Viral Index
            </h3>

            <div className="relative w-64 h-64 flex items-center justify-center">
              {/* Premium Glow Layers */}
              <div className="absolute inset-0 bg-primary/5 rounded-full blur-[40px] animate-pulse" />
              <div className="absolute inset-0 border-[1px] border-white/5 rounded-full scale-125 opacity-30" />

              <svg className="w-full h-full transform -rotate-90">
                <circle
                  className="text-white/[0.03]"
                  cx="128"
                  cy="128"
                  fill="transparent"
                  r="110"
                  stroke="currentColor"
                  strokeWidth="14"
                />
                <motion.circle
                  className="text-primary"
                  cx="128"
                  cy="128"
                  fill="transparent"
                  r="110"
                  stroke="currentColor"
                  strokeWidth="14"
                  strokeDasharray="691.15"
                  initial={{ strokeDashoffset: 691.15 }}
                  animate={{ strokeDashoffset: 691.15 * (1 - viralScore / 100) }}
                  transition={{ duration: 1.5, ease: "easeOut" }}
                  strokeLinecap="round"
                  style={{ filter: "drop-shadow(0 0 20px rgba(221, 183, 255, 0.5))" }}
                />
              </svg>
              <div className="absolute inset-0 flex flex-col items-center justify-center">
                <div className="flex items-baseline">
                   <span className="font-mono text-8xl font-black text-white tracking-tighter">
                      {viralScore}
                   </span>
                   <span className="text-primary text-2xl font-black ml-1">%</span>
                </div>
                <span className="font-mono text-[11px] tracking-[0.4em] text-secondary font-black uppercase mt-2">
                  Optimization Level
                </span>
              </div>
            </div>

            <div className="mt-12 flex items-center gap-3 bg-secondary/10 px-6 py-2 rounded-full border border-secondary/20 shadow-lg">
              <TrendingUp size={16} className="text-secondary" />
              <span className="font-mono text-[11px] font-black text-secondary tracking-widest uppercase">
                CRITICAL MASS REACHED
              </span>
            </div>
          </div>
        </div>

        {/* Daily Summary & Stats */}
        <div className="lg:col-span-7 space-y-8">

          {/* Daily Growth Summary */}
          <div className="glass-card rounded-[3rem] p-10 relative overflow-hidden group">
            <div className="absolute top-0 right-0 p-8 opacity-[0.03] group-hover:opacity-[0.08] transition-opacity duration-700">
              <Activity size={200} />
            </div>

            <div className="flex justify-between items-center mb-8">
               <h3 className="font-display text-2xl font-black text-white">Neural Insights</h3>
               <span className="text-[10px] font-mono font-bold text-white/20 bg-white/5 px-3 py-1 rounded-lg border border-white/5 uppercase tracking-widest">Live 24h</span>
            </div>

            <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
              <SummaryStat label="Reach" value="1.2M" trend="+22%" />
              <SummaryStat label="Saves" value="42.1k" trend="+8.4%" />
              <SummaryStat label="Shares" value="12.8k" trend="+14%" />
              <SummaryStat label="Gains" value="8.4%" trend="+2.1%" />
            </div>

            <div className="mt-12 space-y-6">
              <div className="space-y-3">
                <div className="flex justify-between text-[11px] font-mono font-black uppercase tracking-widest">
                  <span className="text-white/40">Viral Threshold Progression</span>
                  <span className="text-secondary">88% Achieved</span>
                </div>
                <div className="h-2.5 w-full bg-white/5 rounded-full overflow-hidden p-[2px] border border-white/5">
                  <motion.div
                    initial={{ width: 0 }}
                    animate={{ width: "88%" }}
                    transition={{ duration: 2, delay: 0.5, ease: "circOut" }}
                    className="h-full bg-gradient-to-r from-primary via-secondary to-tertiary rounded-full shadow-[0_0_15px_rgba(74,225,118,0.3)]"
                  />
                </div>
              </div>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Quick Action 1 */}
            <div className="glass-card rounded-[2rem] p-8 flex items-center justify-between group cursor-pointer haptic-feedback hover:border-primary/40">
              <div className="flex items-center gap-5">
                <div className="bg-primary/10 p-4 rounded-2xl group-hover:scale-110 transition-transform">
                  <Zap size={28} className="text-primary fill-primary" />
                </div>
                <div>
                  <h4 className="text-base font-black text-white">Smart Triggers</h4>
                  <p className="text-[11px] text-white/30 font-mono uppercase tracking-widest mt-1">3 New Projections</p>
                </div>
              </div>
              <ChevronRight size={22} className="text-white/10 group-hover:text-primary transition-all" />
            </div>

            {/* Quick Action 2 */}
            <div className="glass-card rounded-[2rem] p-8 flex items-center justify-between group cursor-pointer haptic-feedback hover:border-secondary/40">
              <div className="flex items-center gap-5">
                <div className="bg-secondary/10 p-4 rounded-2xl group-hover:scale-110 transition-transform">
                  <Clock size={28} className="text-secondary" />
                </div>
                <div>
                  <h4 className="text-base font-black text-white">Best Post Time</h4>
                  <p className="text-[11px] text-white/30 font-mono uppercase tracking-widest mt-1">In 42 minutes</p>
                </div>
              </div>
              <ChevronRight size={22} className="text-white/10 group-hover:text-secondary transition-all" />
            </div>
          </div>

        </div>
      </div>

      {/* Featured AI Content */}
      <div className="glass-card rounded-[3rem] p-10 border-primary/20 bg-gradient-to-r from-primary/[0.07] to-transparent relative overflow-hidden">
        <div className="absolute -right-20 -top-20 w-80 h-80 bg-primary/10 rounded-full blur-[80px]" />

        <div className="flex flex-col lg:flex-row items-center gap-10 relative z-10">
          <div className="w-24 h-24 rounded-[2rem] bg-primary/20 flex items-center justify-center flex-shrink-0 shadow-2xl">
            <TrendingUp size={48} className="text-primary" />
          </div>
          <div className="space-y-3 text-center lg:text-left">
            <h3 className="font-display text-3xl font-black text-white tracking-tight">Algorithmic Forecast</h3>
            <p className="text-white/60 text-base leading-relaxed max-w-2xl">
              Based on your current trajectory, your next reel has a <span className="text-secondary font-black shadow-[0_0_10px_rgba(74,225,118,0.3)]">74.2% probability</span> of hitting 100k+ views if deployed using the &quot;Contrarian Hook&quot; framework and Midnight Cyber-Synth audio.
            </p>
          </div>
          <button className="bg-primary text-[#490080] px-10 py-5 rounded-[1.5rem] font-black text-xs font-mono uppercase tracking-[0.2em] shadow-[0_15px_30px_rgba(221,183,255,0.3)] hover:scale-105 active:scale-95 transition-all ml-auto">
            DEPLOY NOW
          </button>
        </div>
      </div>
    </div>
  );
}

function SummaryStat({ label, value, trend }: { label: string; value: string; trend: string }) {
  return (
    <div className="space-y-2">
      <span className="font-mono text-[10px] font-black text-white/20 uppercase tracking-[0.2em]">{label}</span>
      <div className="flex flex-col">
        <span className="text-3xl font-black text-white tracking-tight">{value}</span>
        <span className="text-[11px] font-mono font-black text-secondary mt-1">{trend}</span>
      </div>
    </div>
  );
}
