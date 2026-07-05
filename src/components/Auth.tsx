import React, { useState } from "react";
import { motion } from "motion/react";
import { Mail, Lock, LogIn, ChevronRight, Gogle, Cpu, ShieldCheck } from "lucide-react";

export default function Auth({ onLogin }: { onLogin: () => void }) {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // For now, simple bypass. In real app, connect to Firebase JS SDK here.
    onLogin();
  };

  return (
    <div className="min-h-screen bg-[#020617] flex flex-col items-center justify-center p-6 relative overflow-hidden">
      {/* Background Decor */}
      <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] bg-primary/10 rounded-full blur-[120px]" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] bg-secondary/10 rounded-full blur-[120px]" />

      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="w-full max-w-md space-y-8 relative z-10"
      >
        <div className="text-center space-y-4">
          <div className="inline-flex bg-secondary/20 p-4 rounded-[2rem] mb-2 shadow-2xl">
            <Cpu size={48} className="text-secondary" />
          </div>
          <h1 className="font-display text-5xl font-black text-white tracking-tighter italic">
            Paavani <span className="text-secondary">AI</span>
          </h1>
          <p className="text-white/40 text-sm font-mono tracking-widest uppercase">End-to-End Neural Growth</p>
        </div>

        <div className="glass-card rounded-[3rem] p-10 border-white/10 shadow-[0_50px_100px_-20px_rgba(0,0,0,0.5)]">
          <div className="flex gap-4 mb-10 p-1.5 bg-white/5 rounded-2xl border border-white/5">
            <button
              onClick={() => setIsLogin(true)}
              className={`flex-1 py-3 rounded-[1.2rem] text-[10px] font-black font-mono tracking-widest transition-all ${isLogin ? "bg-white text-black shadow-xl" : "text-white/40"}`}
            >
              LOGIN
            </button>
            <button
              onClick={() => setIsLogin(false)}
              className={`flex-1 py-3 rounded-[1.2rem] text-[10px] font-black font-mono tracking-widest transition-all ${!isLogin ? "bg-white text-black shadow-xl" : "text-white/40"}`}
            >
              SIGNUP
            </button>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-2">
              <label className="text-[10px] font-mono font-black text-white/20 uppercase tracking-widest ml-2">Secure Node ID (Email)</label>
              <div className="relative">
                <Mail className="absolute left-4 top-1/2 -translate-y-1/2 text-white/20 w-5 h-5" />
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full bg-white/5 border border-white/10 rounded-2xl py-4 pl-12 pr-6 text-sm focus:border-primary/40 focus:bg-white/10 outline-none transition-all"
                  placeholder="admin@paavani.ai"
                />
              </div>
            </div>

            <div className="space-y-2">
              <label className="text-[10px] font-mono font-black text-white/20 uppercase tracking-widest ml-2">Access Key (Password)</label>
              <div className="relative">
                <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-white/20 w-5 h-5" />
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full bg-white/5 border border-white/10 rounded-2xl py-4 pl-12 pr-6 text-sm focus:border-primary/40 focus:bg-white/10 outline-none transition-all"
                  placeholder="••••••••"
                />
              </div>
            </div>

            <button className="w-full bg-secondary text-[#003915] py-5 rounded-[1.5rem] font-black text-xs font-mono tracking-[0.2em] haptic-feedback flex items-center justify-center gap-3 shadow-[0_15px_30px_rgba(74,225,118,0.25)] mt-10">
              {isLogin ? "AUTHORIZE ACCESS" : "GENERATE IDENTITY"}
              <ChevronRight size={18} />
            </button>
          </form>

          <div className="mt-10 pt-8 border-t border-white/5 flex flex-col items-center gap-6">
             <span className="text-[10px] font-mono font-black text-white/20 uppercase">OR CONNECT WITH</span>
             <button className="w-full bg-white/5 hover:bg-white/10 border border-white/10 py-4 rounded-2xl flex items-center justify-center gap-3 transition-all haptic-feedback cursor-pointer">
                <div className="w-5 h-5 bg-red-500 rounded-sm flex items-center justify-center text-white text-[10px] font-bold">G</div>
                <span className="text-xs font-bold text-white/80">GOOGLE NEURAL ID</span>
             </button>
          </div>
        </div>

        <p className="text-center text-[10px] font-mono text-white/20 font-black uppercase tracking-[0.3em]">
           <ShieldCheck size={12} className="inline mr-2 text-secondary" />
           Paavani Neural Encryption Active
        </p>
      </motion.div>
    </div>
  );
}
