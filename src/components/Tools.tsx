import React, { useState } from "react";
import { motion, AnimatePresence } from "motion/react";
import {
  CheckCircle,
  Sparkles,
  Zap,
  Copy,
  Brain,
  Rocket,
  TrendingUp,
  BarChart,
  ChevronRight,
  ShieldCheck,
  Gauge,
  History
} from "lucide-react";

export default function Tools() {
  // Smart Caption Generator State
  const [captionTopic, setCaptionTopic] = useState("");
  const [activeTone, setActiveTone] = useState<"Funny" | "Professional" | "Controversial" | "Urgent">("Professional");
  const [isGeneratingCaption, setIsGeneratingCaption] = useState(false);
  const [generatedCaption, setGeneratedCaption] = useState<string>(
    `"Stop trading hours for pennies. 💸 Most developers think they're building software, but they're actually building chains. Here's how I used 3 AI agents to automate 40% of my backlog. 🧵👇"`
  );
  const [generatedHashtags, setGeneratedHashtags] = useState<string[]>([
    "#DeveloperLife",
    "#AIRevolution",
    "#Solopreneur",
  ]);

  // Hook Optimization State
  const [draftHook, setDraftHook] = useState("");
  const [isOptimizing, setIsOptimizing] = useState(false);
  const [variantA, setVariantA] = useState({
    text: "Twitter isn't dead. You're just using 2018 strategies in 2024. Stop doing this.",
    lift: "+45% Est.",
  });
  const [variantB, setVariantB] = useState({
    text: "The 'secret' Twitter growth hack nobody talks about (until now).",
    lift: "+32% Est.",
  });

  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const triggerToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 2000);
  };

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text);
    triggerToast("Copied to clipboard!");
  };

  // Caption generator submit logic
  const handleGenerateCaption = async () => {
    const inputTopic = captionTopic.trim() || "AI-powered productivity for developers";
    setIsGeneratingCaption(true);
    try {
      const res = await fetch("/api/generate-caption", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ topic: inputTopic, tone: activeTone }),
      });
      const data = await res.json();
      if (data.caption) {
        setGeneratedCaption(data.caption);
        setGeneratedHashtags(data.hashtags || []);
        triggerToast("New caption generated!");
      }
    } catch (e) {
      console.error(e);
    } finally {
      setIsGeneratingCaption(false);
    }
  };

  // Hook optimizer submit logic
  const handleOptimizeHook = async () => {
    const inputHook = draftHook.trim() || "I found a cool way to grow on Twitter...";
    setIsOptimizing(true);
    try {
      const res = await fetch("/api/optimize-hook", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ draftHook: inputHook }),
      });
      const data = await res.json();
      if (data.variantA && data.variantB) {
        setVariantA(data.variantA);
        setVariantB(data.variantB);
        triggerToast("Hook variants optimized!");
      }
    } catch (e) {
      console.error(e);
    } finally {
      setIsOptimizing(false);
    }
  };

  return (
    <div className="space-y-md animate-fade-in relative pb-16">
      {/* Toast Notification Container */}
      <AnimatePresence>
        {toastMessage && (
          <motion.div
            initial={{ opacity: 0, y: 50, scale: 0.9 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: 30, scale: 0.9 }}
            className="fixed bottom-24 left-1/2 transform -translate-x-1/2 z-50 bg-secondary text-on-secondary px-6 py-3 rounded-full font-bold shadow-lg text-sm flex items-center gap-xs"
          >
            <CheckCircle size={16} />
            {toastMessage}
          </motion.div>
        )}
      </AnimatePresence>

      {/* Hero Section */}
      <section className="mb-lg">
        <div className="relative overflow-hidden rounded-xl p-md glass-card border-primary/20">
          <div className="relative z-10">
            <span className="font-mono text-xs tracking-wider text-secondary mb-base block uppercase font-bold">
              LABORATORY
            </span>
            <h2 className="font-display text-2xl font-bold text-on-surface">Growth Tools</h2>
            <p className="font-sans text-sm text-on-surface-variant max-w-xl mt-xs">
              Harness AI-driven psychographics to engineer content that triggers viral loops and high-retention engagement.
            </p>
          </div>
          {/* Background decoration */}
          <div className="absolute -right-16 -top-16 w-64 h-64 bg-primary/10 rounded-full blur-3xl"></div>
        </div>
      </section>

      {/* Two Column Workspace */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-gutter">
        
        {/* Tool 1: Smart Caption Generator */}
        <div className="lg:col-span-7 flex flex-col h-full">
          <div className="glass-card rounded-xl p-md flex flex-col justify-between h-full border-primary/30 transition-all">
            <div>
              <div className="flex items-center gap-xs mb-md">
                <div className="p-1 bg-primary/20 rounded-lg">
                  <Sparkles className="text-primary" size={18} />
                </div>
                <h3 className="font-display text-lg font-bold text-on-surface">
                  Smart Caption Generator
                </h3>
              </div>

              <div className="space-y-md">
                <div>
                  <label className="font-mono text-[10px] tracking-wider text-on-surface-variant mb-xs block uppercase font-semibold">
                    CONTENT TOPIC / NICHE
                  </label>
                  <input
                    type="text"
                    value={captionTopic}
                    onChange={(e) => setCaptionTopic(e.target.value)}
                    className="w-full bg-[#0F172A] border-none rounded-lg px-sm py-3 text-sm text-on-surface focus:ring-1 focus:ring-primary focus:bg-[#171f33] transition-all outline-none"
                    placeholder="e.g. AI-powered productivity for developers"
                  />
                </div>

                {/* Tone Selectors */}
                <div className="flex gap-xs flex-wrap">
                  {(["Funny", "Professional", "Controversial", "Urgent"] as const).map((t) => (
                    <button
                      key={t}
                      onClick={() => setActiveTone(t)}
                      className={`px-3 py-1 rounded-full border text-[11px] font-mono transition-all cursor-pointer font-bold ${
                        activeTone === t
                          ? "border-primary bg-primary/10 text-primary"
                          : "border-outline-variant/30 text-on-surface-variant hover:border-primary hover:text-primary"
                      }`}
                    >
                      {t}
                    </button>
                  ))}
                </div>

                <button
                  onClick={handleGenerateCaption}
                  disabled={isGeneratingCaption}
                  className="w-full bg-primary text-on-primary font-bold py-3 rounded-lg flex items-center justify-center gap-xs hover:shadow-[0_0_12px_rgba(221,183,255,0.5)] active:scale-[0.98] transition-all cursor-pointer disabled:opacity-50 text-sm"
                >
                  {isGeneratingCaption ? "GENERATING CAPTION..." : "GENERATE CAPTION"}
                  <Zap size={18} />
                </button>
              </div>
            </div>

            {/* Output Area */}
            <div className="mt-md pt-md border-t border-outline-variant/20">
              <div className="bg-[#060e20] rounded-lg p-md relative group border border-outline-variant/10">
                <button
                  onClick={() => copyToClipboard(generatedCaption + " " + generatedHashtags.join(" "))}
                  className="absolute top-xs right-xs opacity-0 group-hover:opacity-100 transition-opacity p-1 bg-surface-bright rounded text-primary hover:text-secondary cursor-pointer"
                >
                  <Copy size={16} />
                </button>
                <p className="font-sans text-sm text-on-surface italic mb-sm leading-relaxed">
                  {generatedCaption}
                </p>
                <div className="flex flex-wrap gap-xs">
                  {generatedHashtags.map((tag, idx) => (
                    <span key={idx} className="text-primary font-mono text-[11px] font-bold">
                      {tag}
                    </span>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Tool 2: Hook Optimization Tool */}
        <div className="lg:col-span-5 flex flex-col gap-md">
          <div className="glass-card rounded-xl p-md flex flex-col justify-between h-full border-secondary/20 transition-all">
            <div>
              <div className="flex items-center gap-xs mb-md">
                <div className="p-1 bg-secondary/20 rounded-lg">
                  <Brain className="text-secondary" size={18} />
                </div>
                <h3 className="font-display text-lg font-bold text-on-surface">Hook Optimization</h3>
              </div>

              <div className="space-y-md">
                <div>
                  <label className="font-mono text-[10px] tracking-wider text-on-surface-variant mb-xs block uppercase font-semibold">
                    YOUR CURRENT HOOK
                  </label>
                  <textarea
                    value={draftHook}
                    onChange={(e) => setDraftHook(e.target.value)}
                    className="w-full bg-[#0F172A] border-none rounded-lg px-sm py-3 text-sm text-on-surface focus:ring-1 focus:ring-secondary focus:bg-[#171f33] transition-all outline-none resize-none text-sm"
                    placeholder="I found a cool way to grow on Twitter..."
                    rows={3}
                  ></textarea>
                </div>

                <button
                  onClick={handleOptimizeHook}
                  disabled={isOptimizing}
                  className="w-full bg-secondary text-on-secondary font-bold py-3 rounded-lg flex items-center justify-center gap-xs hover:shadow-[0_0_12px_rgba(74,225,118,0.5)] active:scale-[0.98] transition-all cursor-pointer disabled:opacity-50 text-sm"
                >
                  {isOptimizing ? "OPTIMIZING FOR RETENTION..." : "OPTIMIZE FOR RETENTION"}
                  <Rocket size={18} />
                </button>
              </div>
            </div>

            <div className="mt-md space-y-sm">
              <label className="font-mono text-[10px] tracking-wider text-secondary block uppercase font-bold">
                AI SUGGESTIONS
              </label>

              {/* Suggestion Card 1 */}
              <div
                onClick={() => copyToClipboard(variantA.text)}
                className="bg-[#131b2e] border-l-4 border-secondary p-sm rounded-r-lg group cursor-pointer hover:bg-[#171f33] transition-all"
              >
                <div className="flex justify-between items-start mb-1">
                  <span className="text-[10px] font-mono text-secondary uppercase font-bold">
                    A/B VARIANT A
                  </span>
                  <span className="text-secondary text-[11px] flex items-center gap-0.5 font-mono font-semibold">
                    <TrendingUp size={12} />
                    {variantA.lift}
                  </span>
                </div>
                <p className="font-sans text-xs text-on-surface leading-relaxed">
                  &quot;{variantA.text}&quot;
                </p>
              </div>

              {/* Suggestion Card 2 */}
              <div
                onClick={() => copyToClipboard(variantB.text)}
                className="bg-[#131b2e] border-l-4 border-primary p-sm rounded-r-lg group cursor-pointer hover:bg-[#171f33] transition-all"
              >
                <div className="flex justify-between items-start mb-1">
                  <span className="text-[10px] font-mono text-primary uppercase font-bold">
                    A/B VARIANT B
                  </span>
                  <span className="text-primary text-[11px] flex items-center gap-0.5 font-mono font-semibold">
                    <TrendingUp size={12} />
                    {variantB.lift}
                  </span>
                </div>
                <p className="font-sans text-xs text-on-surface leading-relaxed">
                  &quot;{variantB.text}&quot;
                </p>
              </div>
            </div>
          </div>

          {/* Action Card templates */}
          <div className="glass-card rounded-xl p-sm flex items-center justify-between group hover:bg-[#222a3d] transition-all cursor-pointer">
            <div className="flex items-center gap-sm">
              <div className="w-10 h-10 rounded-full bg-tertiary/20 flex items-center justify-center">
                <BarChart className="text-tertiary" size={18} />
              </div>
              <div>
                <p className="font-mono text-[10px] tracking-wider text-on-surface uppercase font-bold">
                  View All Templates
                </p>
                <p className="text-[11px] text-on-surface-variant">500+ viral structures</p>
              </div>
            </div>
            <ChevronRight className="text-on-surface-variant group-hover:translate-x-1 transition-transform" />
          </div>
        </div>

        {/* Bento stats row */}
        <div className="lg:col-span-12">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-gutter">
            <div className="glass-card rounded-xl p-md">
              <div className="flex items-center gap-xs mb-xs">
                <ShieldCheck className="text-secondary" size={16} />
                <span className="font-mono text-[10px] tracking-wider text-on-surface-variant uppercase font-semibold">
                  SUCCESS RATE
                </span>
              </div>
              <div className="font-mono text-3xl font-bold text-on-surface mt-base">92.4%</div>
              <div className="w-full h-1 bg-[#2d3449] mt-sm rounded-full overflow-hidden">
                <div className="w-[92%] h-full bg-secondary shadow-[0_0_8px_rgba(74,225,118,0.5)]"></div>
              </div>
            </div>

            <div className="glass-card rounded-xl p-md">
              <div className="flex items-center gap-xs mb-xs">
                <Gauge className="text-primary" size={16} />
                <span className="font-mono text-[10px] tracking-wider text-on-surface-variant uppercase font-semibold">
                  AVG OPTIMIZATION
                </span>
              </div>
              <div className="font-mono text-3xl font-bold text-on-surface mt-base">1.4s</div>
              <p className="text-[11px] text-on-surface-variant mt-xs">Lightning fast generation</p>
            </div>

            <div className="glass-card rounded-xl p-md bg-[#b76dff]/10 border-primary/30">
              <div className="flex items-center gap-xs mb-xs">
                <History className="text-primary" size={16} />
                <span className="font-mono text-[10px] tracking-wider text-on-surface-variant uppercase font-semibold">
                  CREDITS LEFT
                </span>
              </div>
              <div className="flex justify-between items-end mt-base">
                <div className="font-mono text-3xl font-bold text-on-surface">84/100</div>
                <button
                  onClick={() => triggerToast("Credits upgraded to 100/100!")}
                  className="text-primary text-xs font-bold underline mb-1 cursor-pointer hover:text-white"
                >
                  UPGRADE
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
