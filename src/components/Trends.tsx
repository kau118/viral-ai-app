import React, { useState, useEffect } from "react";
import { motion, AnimatePresence } from "motion/react";
import { Search, Play, Music, Flame, Bookmark, ArrowUpRight, Filter, Globe, Instagram, Youtube, Smartphone, CheckCircle2 } from "lucide-react";

export default function Trends() {
  const [activeCategory, setActiveCategory] = useState("Instagram");
  const [loading, setLoading] = useState(true);

  const categories = [
    { name: "Instagram", icon: Instagram },
    { name: "TikTok", icon: Smartphone },
    { name: "YouTube", icon: Youtube },
    { name: "Shorts", icon: Smartphone }
  ];

  useEffect(() => {
    setLoading(true);
    const timer = setTimeout(() => setLoading(false), 1000);
    return () => clearTimeout(timer);
  }, [activeCategory]);

  return (
    <div className="space-y-12 animate-fade-in">
      {/* Header & Search */}
      <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-8">
        <div className="space-y-2">
          <div className="flex items-center gap-3">
             <div className="bg-secondary/20 p-1.5 rounded-lg">
                <Globe size={16} className="text-secondary" />
             </div>
             <span className="text-[10px] font-mono font-black text-secondary uppercase tracking-[0.4em]">Global Network Sync</span>
          </div>
          <h2 className="font-display text-5xl font-black text-white italic">Trend <span className="text-secondary">Pulse</span></h2>
          <p className="text-white/40 text-sm max-w-sm">AI is currently auditing viral clusters in 142 territories.</p>
        </div>

        <div className="flex items-center gap-4 w-full lg:w-auto">
          <div className="relative flex-1 lg:w-80 group">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-white/20 group-focus-within:text-secondary transition-colors w-5 h-5" />
            <input
              type="text"
              placeholder="Search hashtags or niches..."
              className="w-full bg-[#0b1326] border border-white/10 rounded-[1.5rem] py-4 pl-12 pr-6 text-sm focus:border-secondary/40 focus:bg-[#0f172a] outline-none transition-all shadow-2xl"
            />
          </div>
          <button className="p-4 bg-[#0b1326] border border-white/10 rounded-2xl text-white/40 hover:text-white hover:border-white/20 transition-all haptic-feedback cursor-pointer">
            <Filter size={20} />
          </button>
        </div>
      </div>

      {/* Category Chips */}
      <div className="flex gap-3 overflow-x-auto pb-4 custom-scrollbar">
        {categories.map(cat => {
           const isActive = activeCategory === cat.name;
           return (
            <button
              key={cat.name}
              onClick={() => setActiveCategory(cat.name)}
              className={`flex items-center gap-3 px-8 py-3.5 rounded-[1.2rem] text-xs font-black font-mono tracking-widest transition-all haptic-feedback cursor-pointer whitespace-nowrap border ${
                isActive
                  ? "bg-secondary text-[#003915] border-secondary shadow-[0_15px_30px_rgba(74,225,118,0.25)]"
                  : "bg-white/5 text-white/40 border-white/5 hover:border-white/20 hover:text-white"
              }`}
            >
              <cat.icon size={16} />
              {cat.name.toUpperCase()}
            </button>
           );
        })}
      </div>

      {/* Trending Reels Grid */}
      <section className="space-y-8">
        <div className="flex justify-between items-center px-2">
          <div className="flex items-center gap-4">
             <div className="bg-orange-500/20 p-2.5 rounded-2xl">
                <Flame size={24} className="text-orange-500 fill-orange-500" />
             </div>
             <h3 className="font-display text-3xl font-black text-white tracking-tight">Viral Reels</h3>
          </div>
          <button className="text-[10px] font-black font-mono text-primary tracking-[0.2em] flex items-center gap-2 group hover:text-white transition-colors">
            AUDIT ALL <ArrowUpRight size={16} className="group-hover:translate-x-0.5 group-hover:-translate-y-0.5 transition-transform" />
          </button>
        </div>

        <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
          {loading ? (
             [1,2,3,4].map(i => <div key={i} className="aspect-[9/16] rounded-[2.5rem] skeleton" />)
          ) : (
            [1, 2, 3, 4].map(i => (
              <div key={i} className="aspect-[9/16] rounded-[2.5rem] overflow-hidden relative group cursor-pointer border border-white/5 shadow-2xl haptic-feedback">
                <img
                  src={`https://images.unsplash.com/photo-1620${121692029 + i}?w=500&q=80&auto=format&fit=crop`}
                  className="w-full h-full object-cover transition-transform duration-1000 group-hover:scale-110"
                  alt="Trend"
                />
                <div className="absolute inset-0 bg-gradient-to-t from-[#020617] via-transparent to-transparent opacity-80 group-hover:opacity-90 transition-opacity" />

                <div className="absolute top-4 right-4">
                   <div className="bg-black/20 backdrop-blur-md p-2 rounded-xl border border-white/10">
                      <Bookmark size={14} className="text-white/60 group-hover:text-primary transition-colors" />
                   </div>
                </div>

                <div className="absolute bottom-6 left-6 right-6 space-y-3">
                  <div className="flex items-center gap-2">
                    <span className="bg-secondary/20 backdrop-blur-md text-secondary text-[9px] font-black px-3 py-1 rounded-full border border-secondary/30 tracking-widest uppercase">
                      +{(400 + (i * 12))}% Spike
                    </span>
                  </div>
                  <p className="text-sm font-bold text-white line-clamp-2 leading-tight tracking-tight">Neural Marketing strategies for modern brands.</p>
                </div>

                <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-all duration-500">
                  <div className="w-16 h-16 bg-white/20 backdrop-blur-2xl rounded-full flex items-center justify-center border border-white/30 scale-75 group-hover:scale-100 transition-transform shadow-2xl">
                    <Play size={28} className="text-white fill-white ml-1" />
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </section>

      {/* Audio & Hooks Bento */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">

        {/* Trending Audio */}
        <div className="glass-card rounded-[3rem] p-10 space-y-8 border-white/10 relative overflow-hidden group">
           <div className="absolute -right-20 -top-20 w-64 h-64 bg-secondary/5 rounded-full blur-[80px]" />

          <div className="flex justify-between items-center relative z-10">
            <h3 className="font-display text-2xl font-black flex items-center gap-4 text-white">
              <div className="bg-secondary/20 p-2.5 rounded-2xl">
                 <Music size={24} className="text-secondary" />
              </div>
              Velocity Audio
            </h3>
            <span className="text-[10px] font-mono font-black text-secondary tracking-widest uppercase bg-secondary/10 px-3 py-1 rounded-lg">High Momentum</span>
          </div>

          <div className="space-y-4 relative z-10">
            {[
              { name: "Midnight Cyber-Synth", artist: "SynthWave Collective", velocity: "+412%" },
              { name: "Organic Pulse Beta", artist: "Algorithmic Beats", velocity: "+288%" },
              { name: "Growth Engine #4", artist: "Data Driven", velocity: "+124%" },
            ].map((audio, i) => (
              <div key={i} className="flex items-center justify-between p-5 bg-white/[0.03] rounded-3xl border border-white/5 group hover:border-secondary/30 hover:bg-white/[0.06] transition-all cursor-pointer haptic-feedback">
                <div className="flex items-center gap-5">
                  <div className="relative w-16 h-16 rounded-2xl overflow-hidden shadow-2xl">
                    <img src={`https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c${17 + i}?w=150&q=80`} className="w-full h-full object-cover" alt="Audio" />
                    <div className="absolute inset-0 flex items-center justify-center bg-black/40 group-hover:bg-black/20 transition-colors">
                      <Play size={20} className="text-white fill-white" />
                    </div>
                  </div>
                  <div>
                    <h4 className="text-base font-bold text-white">{audio.name}</h4>
                    <p className="text-[10px] text-white/40 font-mono tracking-widest uppercase mt-1">{audio.artist}</p>
                  </div>
                </div>
                <div className="text-right">
                  <span className="text-secondary font-mono text-lg font-black">{audio.velocity}</span>
                  <p className="text-[9px] text-white/20 font-black uppercase tracking-widest">Velocity</p>
                </div>
              </div>
            ))}
          </div>

          <button className="w-full py-4 bg-white/5 border border-white/10 rounded-[1.5rem] font-black font-mono text-[10px] text-white/60 tracking-widest uppercase hover:text-white hover:bg-white/10 transition-all haptic-feedback">
             EXPLORE FULL LIBRARY
          </button>
        </div>

        {/* Viral Hooks Library */}
        <div className="glass-card rounded-[3rem] p-10 space-y-8 border-white/10 relative overflow-hidden group">
          <div className="flex justify-between items-center relative z-10">
            <h3 className="font-display text-2xl font-black flex items-center gap-4 text-white">
              <div className="bg-primary/20 p-2.5 rounded-2xl">
                 <Bookmark size={24} className="text-primary fill-primary" />
              </div>
              Hook Repository
            </h3>
            <button className="text-[10px] font-mono font-black text-primary tracking-widest uppercase hover:underline">Saved: 14</button>
          </div>

          <div className="grid grid-cols-1 gap-4 relative z-10">
            {[
              { text: "Most founders think scaling is about more leads. It's actually about doing this one thing that feels illegal.", score: 92, tag: "HIGH RETENTION" },
              { text: "Stop scrolling if you're still using LinkedIn for leads. You're leaving $5k on the table every month.", score: 85, tag: "CURIOSITY GAP" },
              { text: "I spent $10k on ads so you don't have to. Here is the exact blueprint I used to 10x ROI.", score: 98, tag: "SECRET BLUEPRINT" },
            ].map((hook, i) => (
              <div key={i} className="p-6 bg-white/[0.03] rounded-3xl border border-white/5 relative group cursor-pointer hover:bg-white/[0.07] transition-all haptic-feedback">
                <div className="flex justify-between items-start mb-4">
                  <span className="bg-primary/10 text-primary text-[9px] font-black px-3 py-1 rounded-full border border-primary/20 tracking-widest uppercase">
                    {hook.tag}
                  </span>
                  <div className="bg-white/5 p-2 rounded-xl group-hover:bg-primary/20 transition-all">
                     <Bookmark size={16} className="text-white/20 group-hover:text-primary group-hover:fill-primary transition-colors" />
                  </div>
                </div>
                <p className="text-base text-white/90 font-medium italic leading-relaxed">&quot;{hook.text}&quot;</p>
                <div className="mt-6 flex items-center gap-4">
                  <div className="flex-1 h-1.5 bg-white/5 rounded-full overflow-hidden p-[1px]">
                    <div className="h-full bg-primary rounded-full shadow-[0_0_10px_#ddb7ff]" style={{ width: `${hook.score}%` }} />
                  </div>
                  <span className="text-[11px] font-mono font-black text-primary">{hook.score}% Score</span>
                </div>
              </div>
            ))}
          </div>
        </div>

      </div>
    </div>
  );
}
