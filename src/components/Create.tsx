import React, { useState } from "react";
import { motion, AnimatePresence } from "motion/react";
import {
  Sparkles,
  Zap,
  Hash,
  Film,
  MessageSquare,
  Copy,
  Save,
  RefreshCw,
  CheckCircle2,
  ChevronRight,
  ClipboardCheck,
  Send,
  Loader2
} from "lucide-react";
import { CreateSubTab } from "../types";
import { API_BASE } from "../constants";

interface GeneratedContent {
  hook: string;
  caption: string;
  script: string;
  hashtags: string[];
}

export default function Create() {
  const [activeSubTab, setActiveSubTab] = useState<CreateSubTab>("hooks");
  const [topic, setTopic] = useState("");
  const [vibe, setVibe] = useState("Professional");
  const [loading, setLoading] = useState(false);
  const [toast, setToast] = useState<string | null>(null);

  const [result, setResult] = useState<GeneratedContent | null>(null);

  const subTabs = [
    { id: "hooks", label: "Hooks", icon: Zap },
    { id: "captions", label: "Captions", icon: MessageSquare },
    { id: "hashtags", label: "Hashtags", icon: Hash },
    { id: "scripts", label: "Scripts", icon: Film },
  ];

  const showToast = (msg: string) => {
    setToast(msg);
    setTimeout(() => setToast(null), 2500);
  };

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text);
    showToast("Copied to clipboard!");
  };

  const handleGenerate = async () => {
    if (!topic.trim()) {
      showToast("Please enter a topic first!");
      return;
    }

    setLoading(true);
    setResult(null);

    try {
      const response = await fetch(`${API_BASE}/api/generate-all`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ topic, vibe }),
      });

      if (!response.ok) throw new Error("API Connection Failed");

      const data = await response.json();
      setResult(data);
      showToast("AI Package Generated!");
    } catch (error) {
      console.error(error);
      showToast("Error: Backend not connected.");

      // Fallback for demo if backend is not available
      setResult({
        hook: "Wait, before you scroll—did you know that most salon owners miss this one viral trigger?",
        caption: "Opening a new space isn't just about the décor, it's about the vibe. ✨ Come experience the luxury at Paavani Salon. ✂️ Book your spot now! #SalonLife #GlowUp",
        script: "[Scene 1]: Fast cuts of the new interior. [VO]: The wait is over. [Scene 2]: Close up of a client's transformation. [VO]: Welcome to the new era of beauty.",
        hashtags: ["#NewSalon", "#BeautyTrends", "#PaavaniAI", "#GlowUp", "#HairTransformation"]
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-8 animate-fade-in pb-12">
      {/* Toast */}
      <AnimatePresence>
        {toast && (
          <motion.div
            initial={{ opacity: 0, y: 50, scale: 0.9 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: 30, scale: 0.9 }}
            className="fixed bottom-28 left-1/2 -translate-x-1/2 z-[100] bg-secondary text-on-secondary px-6 py-3 rounded-full font-bold shadow-2xl flex items-center gap-2"
          >
            <CheckCircle2 size={18} />
            {toast}
          </motion.div>
        )}
      </AnimatePresence>

      <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
        <div>
          <h2 className="font-display text-4xl font-black text-white italic">Create <span className="text-secondary">Engine</span></h2>
          <p className="text-white/40 text-sm mt-1">Transform any idea into a viral master-plan.</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
        {/* Editor Area */}
        <div className="lg:col-span-5 space-y-6">
          <div className="glass-card rounded-[2.5rem] p-8 space-y-8 border-white/10 relative overflow-hidden">
             <div className="absolute top-0 right-0 p-6 opacity-[0.05] pointer-events-none">
                <Sparkles size={120} className="text-primary" />
             </div>

            <div className="space-y-4">
              <label className="font-mono text-[10px] font-black text-white/40 uppercase tracking-[0.3em] block">Campaign Objective</label>
              <div className="relative">
                <textarea
                  value={topic}
                  onChange={(e) => setTopic(e.target.value)}
                  placeholder="e.g. Salon opening reel, 5 coffee hacks, gym motivation..."
                  className="w-full h-44 bg-white/5 border border-white/10 rounded-[2rem] p-6 text-base focus:bg-white/10 focus:border-secondary/40 outline-none transition-all resize-none custom-scrollbar placeholder:text-white/10"
                />
                <div className="absolute bottom-4 right-6 flex items-center gap-2">
                   <span className="text-[10px] font-mono text-white/20">{topic.length}/500</span>
                </div>
              </div>
            </div>

            <div className="space-y-4">
              <label className="font-mono text-[10px] font-black text-white/40 uppercase tracking-[0.3em] block">Strategic Tone</label>
              <div className="grid grid-cols-3 gap-2">
                {["Professional", "Dramatic", "Funny"].map(t => (
                  <button
                    key={t}
                    onClick={() => setVibe(t)}
                    className={`py-3 rounded-2xl border font-black font-mono text-[10px] tracking-widest transition-all haptic-feedback cursor-pointer ${
                      vibe === t ? "bg-secondary/10 border-secondary text-secondary shadow-[0_0_20px_rgba(74,225,118,0.15)]" : "border-white/5 text-white/30 hover:border-white/20"
                    }`}
                  >
                    {t.toUpperCase()}
                  </button>
                ))}
              </div>
            </div>

            <button
              onClick={handleGenerate}
              disabled={loading}
              className="w-full bg-secondary text-[#003915] py-5 rounded-[1.5rem] font-black text-xs font-mono tracking-[0.2em] haptic-feedback flex items-center justify-center gap-3 shadow-[0_15px_35px_rgba(74,225,118,0.3)] disabled:opacity-50 disabled:cursor-not-allowed group transition-all"
            >
              {loading ? <Loader2 className="animate-spin" size={20} /> : <Zap size={20} className="group-hover:scale-110 group-hover:rotate-12 transition-transform" />}
              {loading ? "AI IS THINKING..." : "ACTIVATE ENGINE"}
            </button>
          </div>
        </div>

        {/* Results Area */}
        <div className="lg:col-span-7 space-y-8">
          {!result && !loading ? (
             <div className="h-full min-h-[400px] flex flex-col items-center justify-center text-center p-12 glass-card rounded-[3rem] border-dashed border-white/10">
                <div className="w-20 h-20 bg-white/5 rounded-full flex items-center justify-center mb-6">
                   <Send size={32} className="text-white/20" />
                </div>
                <h3 className="text-xl font-black text-white/60">Ready to Launch</h3>
                <p className="text-white/30 text-sm mt-2 max-w-xs">Input your topic and the AI will generate your full viral package here.</p>
             </div>
          ) : (
             <div className="space-y-6">
                <div className="flex justify-between items-center px-4">
                  <h3 className="font-mono text-[10px] font-black text-white/40 uppercase tracking-[0.3em]">Neural Output Package</h3>
                  <div className="flex items-center gap-4">
                     <span className="flex items-center gap-2 text-[10px] font-mono text-secondary font-black">
                        <div className="w-1.5 h-1.5 rounded-full bg-secondary animate-pulse" />
                        OPTIMIZED
                     </span>
                  </div>
                </div>

                <div className="space-y-4 max-h-[700px] overflow-y-auto pr-2 custom-scrollbar">
                   {/* HOOK CARD */}
                   <ResultCard
                      label="The Hook (First 3s)"
                      content={result?.hook || ""}
                      onCopy={() => copyToClipboard(result?.hook || "")}
                      icon={Zap}
                      color="text-secondary"
                      loading={loading}
                   />

                   {/* CAPTION CARD */}
                   <ResultCard
                      label="Viral Caption"
                      content={result?.caption || ""}
                      onCopy={() => copyToClipboard(result?.caption || "")}
                      icon={MessageSquare}
                      color="text-primary"
                      loading={loading}
                   />

                   {/* SCRIPT CARD */}
                   <ResultCard
                      label="Production Script"
                      content={result?.script || ""}
                      onCopy={() => copyToClipboard(result?.script || "")}
                      icon={Film}
                      color="text-tertiary"
                      loading={loading}
                   />

                   {/* HASHTAGS */}
                   <motion.div
                      initial={{ opacity: 0, y: 10 }}
                      animate={{ opacity: 1, y: 0 }}
                      className="glass-card rounded-3xl p-6 border-white/10"
                   >
                      <span className="text-[10px] font-mono font-black text-white/30 uppercase tracking-widest block mb-4">Optimized Clusters</span>
                      <div className="flex flex-wrap gap-2">
                        {loading ? (
                           [1,2,3,4,5].map(i => <div key={i} className="h-6 w-20 skeleton rounded-lg" />)
                        ) : (
                           result?.hashtags.map(tag => (
                            <span
                              key={tag}
                              onClick={() => copyToClipboard(tag)}
                              className="px-4 py-1.5 bg-white/5 hover:bg-white/10 border border-white/10 rounded-full text-xs font-mono font-bold text-secondary cursor-pointer transition-all"
                            >
                              {tag}
                            </span>
                           ))
                        )}
                      </div>
                   </motion.div>
                </div>
             </div>
          )}
        </div>
      </div>
    </div>
  );
}

function ResultCard({ label, content, onCopy, icon: Icon, color, loading }: any) {
  return (
    <motion.div
      initial={{ opacity: 0, x: 20 }}
      animate={{ opacity: 1, x: 0 }}
      className="glass-card rounded-[2rem] p-7 space-y-4 border-white/5 relative group"
    >
      <div className="flex justify-between items-start">
        <div className="flex items-center gap-3">
          <div className={`${color.replace('text-', 'bg-')}/20 p-2 rounded-xl`}>
             <Icon size={16} className={color} />
          </div>
          <span className="text-[10px] font-mono font-black text-white/40 uppercase tracking-widest">{label}</span>
        </div>
        <button
          onClick={onCopy}
          className="p-2.5 hover:bg-white/5 rounded-xl text-white/20 hover:text-white transition-all cursor-pointer group"
        >
          <ClipboardCheck size={18} className="group-hover:scale-110 transition-transform" />
        </button>
      </div>

      {loading ? (
         <div className="space-y-2">
            <div className="h-4 w-full skeleton rounded-lg" />
            <div className="h-4 w-3/4 skeleton rounded-lg" />
         </div>
      ) : (
         <p className="text-white/90 leading-relaxed text-sm font-medium italic">
            &quot;{content}&quot;
         </p>
      )}
    </motion.div>
  );
}
