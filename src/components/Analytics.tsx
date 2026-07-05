import React, { useState } from "react";
import { motion, AnimatePresence } from "motion/react";
import { BarChart3, TrendingUp, Clock, Calendar, ArrowUpRight, Zap, Target, Instagram, Link2, Lock, ChevronRight } from "lucide-react";

export default function Analytics() {
  const [isConnected, setIsConnected] = useState(false);

  return (
    <div className="space-y-10 animate-fade-in pb-12">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-end justify-between gap-6">
        <div>
          <h2 className="font-display text-4xl font-black text-white italic">Algorithm <span className="text-primary">Insights</span></h2>
          <p className="text-white/40 text-sm mt-1">Deep analysis of your content ecosystem.</p>
        </div>

        {isConnected && (
           <div className="flex bg-white/5 p-1 rounded-2xl border border-white/5 h-fit">
            {["7D", "30D", "ALL"].map(t => (
              <button key={t} className={`px-5 py-2 rounded-xl text-[10px] font-black font-mono tracking-widest transition-all ${t === "7D" ? "bg-white/10 text-white" : "text-white/30 hover:text-white"}`}>
                {t}
              </button>
            ))}
          </div>
        )}
      </div>

      {!isConnected ? (
        <div className="glass-card rounded-[3rem] p-12 flex flex-col items-center justify-center text-center space-y-8 min-h-[500px] border-primary/20 relative overflow-hidden">
           <div className="absolute top-0 left-0 w-full h-full bg-[radial-gradient(circle_at_center,rgba(221,183,255,0.05)_0%,transparent_70%)]" />

           <div className="relative">
              <div className="w-24 h-24 bg-gradient-to-tr from-primary to-secondary rounded-[2rem] flex items-center justify-center shadow-[0_20px_50px_rgba(221,183,255,0.3)] rotate-12">
                 <Instagram size={48} className="text-white -rotate-12" />
              </div>
              <div className="absolute -bottom-2 -right-2 bg-[#020617] p-2 rounded-full">
                 <div className="bg-white/5 p-2 rounded-full">
                    <Lock size={16} className="text-white/40" />
                 </div>
              </div>
           </div>

           <div className="max-w-md space-y-4 relative z-10">
              <h3 className="text-3xl font-black text-white tracking-tight">Connect your Audience</h3>
              <p className="text-white/40 text-base leading-relaxed">
                 Paavani AI needs secure access to your Instagram Insights to calculate real-time virality scores and growth projections.
              </p>
           </div>

           <div className="flex flex-col sm:flex-row items-center gap-4 w-full max-w-sm relative z-10">
              <button
                onClick={() => setIsConnected(true)}
                className="w-full bg-white text-black py-5 rounded-[1.5rem] font-black text-xs font-mono tracking-[0.2em] haptic-feedback flex items-center justify-center gap-3 shadow-2xl transition-all hover:scale-105 active:scale-95"
              >
                <Link2 size={18} />
                AUTHORIZE INSTAGRAM
              </button>
           </div>

           <div className="flex items-center gap-6 text-[10px] font-mono font-black text-white/20 tracking-widest uppercase">
              <span className="flex items-center gap-2">🛡️ SECURE ENCRYPTION</span>
              <span className="flex items-center gap-2">🤖 AI AUDITING ONLY</span>
           </div>
        </div>
      ) : (
        <>
          {/* Main Stats Grid */}
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Engagement Chart Mock */}
            <div className="lg:col-span-2 glass-card rounded-[3rem] p-10 border-white/10 space-y-10">
              <div className="flex justify-between items-center">
                <h3 className="font-display text-2xl font-bold flex items-center gap-4 text-white">
                  <div className="bg-primary/20 p-2 rounded-xl">
                    <BarChart3 size={24} className="text-primary" />
                  </div>
                  Engagement Velocity
                </h3>
                <div className="flex items-center gap-3">
                  <span className="w-2.5 h-2.5 rounded-full bg-primary shadow-[0_0_10px_#ddb7ff]" />
                  <span className="text-[11px] font-mono text-white/40 font-black uppercase tracking-widest">Global Reach</span>
                </div>
              </div>

              <div className="h-72 flex items-end justify-between gap-4 px-6 relative">
                {/* Horizontal grid lines */}
                <div className="absolute inset-0 flex flex-col justify-between pointer-events-none py-2">
                   {[1,2,3,4].map(i => <div key={i} className="w-full border-t border-white/[0.03]" />)}
                </div>

                {[40, 65, 30, 85, 100, 55, 75].map((h, i) => (
                  <div key={i} className="flex-1 flex flex-col items-center gap-6 group relative z-10">
                    <motion.div
                      initial={{ height: 0 }}
                      animate={{ height: `${h}%` }}
                      transition={{ duration: 1.2, delay: i * 0.1, ease: "circOut" }}
                      className={`w-full rounded-t-2xl transition-all relative ${h === 100 ? "bg-primary premium-glow-purple" : "bg-white/5 group-hover:bg-white/10"}`}
                    >
                      <div className="absolute -top-12 left-1/2 -translate-x-1/2 opacity-0 group-hover:opacity-100 transition-all bg-white/10 backdrop-blur-xl border border-white/20 px-3 py-1.5 rounded-xl text-[10px] font-mono font-black shadow-2xl">
                        {(h * 14.2).toFixed(1)}k
                      </div>
                    </motion.div>
                    <span className="text-[10px] font-mono text-white/20 font-black tracking-widest uppercase">
                       {["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"][i]}
                    </span>
                  </div>
                ))}
              </div>
            </div>

            {/* Best Posting Time */}
            <div className="glass-card rounded-[3rem] p-10 border-secondary/20 bg-secondary/[0.03] flex flex-col justify-between relative overflow-hidden group">
               <div className="absolute -right-10 -top-10 w-40 h-40 bg-secondary/10 rounded-full blur-[60px]" />

              <div className="space-y-8 relative z-10">
                <div className="bg-secondary/20 p-5 rounded-[2rem] w-fit shadow-2xl">
                  <Clock size={36} className="text-secondary" />
                </div>
                <div>
                  <h3 className="font-display text-3xl font-black text-white leading-tight">Optimal <br />Window</h3>
                  <p className="text-white/40 text-sm mt-3 leading-relaxed">The algorithm has identified a peak engagement spike starting in:</p>
                </div>
              </div>

              <div className="space-y-4 mt-10 relative z-10">
                <div className="p-6 bg-[#0b1326]/80 backdrop-blur-2xl rounded-[2rem] border border-white/10 shadow-2xl">
                  <div className="flex justify-between items-center mb-2">
                    <span className="text-[10px] font-mono font-black text-secondary uppercase tracking-[0.3em]">AI PROJECTION</span>
                    <span className="text-white font-mono text-xs font-black bg-white/5 px-2 py-0.5 rounded-lg">42:15m</span>
                  </div>
                  <p className="text-4xl font-black text-white tabular-nums tracking-tighter">8:45 <span className="text-sm font-black text-white/20 tracking-widest uppercase ml-1">PM</span></p>
                </div>
                <button className="w-full bg-secondary text-[#003915] py-4 rounded-[1.5rem] font-black text-[10px] font-mono tracking-[0.2em] uppercase haptic-feedback shadow-[0_10px_25px_rgba(74,225,118,0.25)]">
                  ENABLE AUTO-NOTIFY
                </button>
              </div>
            </div>
          </div>

          {/* Prediction & Weekly Performance */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">

            {/* Growth Prediction */}
            <div className="glass-card rounded-[3rem] p-10 border-white/10 relative overflow-hidden group">
              <div className="absolute top-0 right-0 p-8 opacity-[0.03] group-hover:opacity-[0.1] transition-opacity duration-700">
                <Target size={150} />
              </div>

              <h3 className="font-display text-2xl font-black text-white mb-8 flex items-center gap-4">
                <div className="bg-tertiary/10 p-2 rounded-xl">
                   <TrendingUp size={24} className="text-tertiary" />
                </div>
                Growth Forecast
              </h3>

              <div className="space-y-8 relative z-10">
                <div className="flex items-baseline gap-3">
                  <span className="text-6xl font-black text-white tracking-tighter">+24.8%</span>
                  <span className="text-[10px] font-mono font-black text-secondary bg-secondary/10 px-3 py-1 rounded-full border border-secondary/20 uppercase tracking-widest">Projected</span>
                </div>
                <p className="text-white/40 text-base leading-relaxed max-w-sm">
                  Neural models predict a major breakout in your <span className="text-white font-black">Lifestyle-Tech</span> content cluster.
                </p>
                <div className="space-y-3 pt-2">
                  <div className="flex justify-between items-center text-[10px] font-mono font-black uppercase tracking-widest">
                     <span className="text-white/20">Confidence Rating</span>
                     <span className="text-tertiary">65% Reliable</span>
                  </div>
                  <div className="h-2 w-full bg-white/5 rounded-full overflow-hidden">
                    <div className="h-full bg-tertiary rounded-full shadow-[0_0_10px_#4cd7f6]" style={{ width: "65%" }} />
                  </div>
                </div>
              </div>
            </div>

            {/* AI Weekly Suggestions */}
            <div className="glass-card rounded-[3rem] p-10 border-primary/20 bg-primary/[0.03]">
               <div className="flex justify-between items-center mb-8">
                  <h3 className="font-display text-2xl font-black text-white flex items-center gap-4">
                    <div className="bg-primary/20 p-2 rounded-xl">
                      <Zap size={24} className="text-primary fill-primary" />
                    </div>
                    Growth Roadmap
                  </h3>
                  <button className="text-[10px] font-mono font-black text-primary tracking-widest uppercase hover:underline">Full Strategy</button>
               </div>

              <div className="space-y-4">
                {[
                  { text: "Switch to #midjourneyv6 tags immediately for 2.4x reach.", icon: Zap },
                  { text: "Reduce hook length by 0.5s for 15% more retention.", icon: Target },
                  { text: "Engagement Loop: Reply to 5 top comments in 30 mins.", icon: BarChart3 },
                ].map((suggest, i) => (
                  <div key={i} className="flex items-start gap-5 p-5 bg-white/[0.03] rounded-[1.5rem] border border-white/5 group hover:border-primary/40 transition-all cursor-default">
                    <div className="mt-1">
                       <suggest.icon size={18} className="text-primary opacity-60" />
                    </div>
                    <p className="text-sm text-white/80 leading-relaxed font-semibold">{suggest.text}</p>
                  </div>
                ))}
              </div>
            </div>

          </div>
        </>
      )}
    </div>
  );
}
