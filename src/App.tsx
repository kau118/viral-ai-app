import React, { useState, useEffect } from "react";
import { motion, AnimatePresence } from "motion/react";
import {
  LayoutDashboard,
  TrendingUp,
  Wand2,
  BarChart3,
  User,
  Bell,
  Search,
  Cpu
} from "lucide-react";
import { MainTab } from "./types";
import Dashboard from "./components/Dashboard";
import Create from "./components/Create";
import Trends from "./components/Trends";
import Analytics from "./components/Analytics";
import Profile from "./components/Profile";
import Auth from "./components/Auth";

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [activeTab, setActiveTab] = useState<MainTab>("dashboard");
  const [isAlertOpen, setIsAlertOpen] = useState(false);
  const [alertContent, setAlertContent] = useState("");

  useEffect(() => {
    if (!isAuthenticated) return;
    const alerts = [
      "Viral Alert: Midnight Cyber-Synth spiked +412%",
      "Strategy: Use #midjourneyv6 for 2x reach",
      "Analysis: POV hooks increasing retention",
    ];
    let counter = 0;
    const timer = setInterval(() => {
      setAlertContent(alerts[counter % alerts.length]);
      setIsAlertOpen(true);
      counter++;
      setTimeout(() => setIsAlertOpen(false), 4000);
    }, 20000);
    return () => clearInterval(timer);
  }, []);

  const tabs = [
    { id: "dashboard", label: "Home", icon: LayoutDashboard },
    { id: "trends", label: "Trends", icon: TrendingUp },
    { id: "create", label: "Create", icon: Wand2 },
    { id: "analytics", label: "Stats", icon: BarChart3 },
    { id: "profile", label: "Me", icon: User },
  ];

  if (!isAuthenticated) {
    return <Auth onLogin={() => setIsAuthenticated(true)} />;
  }

  return (
    <div className="min-h-screen bg-[#020617] text-[#f1f5f9] font-sans overflow-x-hidden">
      
      {/* Premium Header */}
      <header className="sticky top-0 z-50 bg-[#020617]/70 backdrop-blur-3xl border-b border-white/5 px-6 py-4">
        <div className="max-w-7xl mx-auto flex items-center justify-between">
          
          <div className="flex items-center gap-3">
            <div className="bg-secondary/20 p-2 rounded-xl border border-secondary/30">
              <Cpu className="text-secondary w-5 h-5" />
            </div>
            <div className="hidden sm:flex flex-col">
              <span className="font-display text-lg font-black tracking-tight text-white leading-none">
                PAAVANI<span className="text-secondary">AI</span>
              </span>
              <div className="flex items-center gap-1.5 mt-1">
                <div className="w-1.5 h-1.5 rounded-full bg-secondary animate-pulse" />
                <span className="font-mono text-[8px] tracking-[0.2em] text-secondary font-black uppercase">
                  AI Active
                </span>
              </div>
            </div>
          </div>

          <div className="flex-1 max-w-sm px-8 hidden md:block">
            <div className="relative group">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-white/20 w-4 h-4" />
              <input
                type="text"
                placeholder="Find viral triggers..."
                className="w-full bg-white/5 border border-white/10 rounded-full py-2 pl-10 pr-4 text-xs focus:bg-white/10 transition-all outline-none"
              />
            </div>
          </div>

          <div className="flex items-center gap-4">
            <button className="relative p-2 hover:bg-white/5 rounded-full transition-colors cursor-pointer text-white/60">
              <Bell size={20} />
              <span className="absolute top-2 right-2 w-2 h-2 bg-primary rounded-full border-2 border-[#020617] shadow-[0_0_8px_#ddb7ff]" />
            </button>
            <div
              onClick={() => setActiveTab("profile")}
              className="w-9 h-9 rounded-full border border-primary/30 p-0.5 cursor-pointer hover:scale-105 transition-transform"
            >
              <img
                src="https://lh3.googleusercontent.com/aida-public/AB6AXuB9CH_A3Unxpt8r-v3qDuIOwq4R7AGeES4xgNkxFVPGsIPvusHKWleESgTFqpqr8DLsj2lMzodw_oHENf7YIcmVvR2ONWJfQLn5Rg5DKsdpswgnxS5ujAx5U33khbcUkuZ6No0heNTcMrx4BQGeI2zuqXcoWcv_BDTD9u-ta92Ah43EWsLXfQm8KyfMSNBalX13ED_QuuQJJfVjo-INLmXK9-fahlGxVRefcDp-9-5o82V0gejxZ-6XjVJTx-Nb63_gvkfvYy9i01hW"
                className="w-full h-full rounded-full object-cover"
                alt="Avatar"
              />
            </div>
          </div>

        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-6 pt-6 pb-32">
        <AnimatePresence mode="wait">
          <motion.div
            key={activeTab}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            transition={{ duration: 0.4, ease: [0.16, 1, 0.3, 1] }}
          >
            {activeTab === "dashboard" && <Dashboard />}
            {activeTab === "trends" && <Trends />}
            {activeTab === "create" && <Create />}
            {activeTab === "analytics" && <Analytics />}
            {activeTab === "profile" && <Profile />}
          </motion.div>
        </AnimatePresence>
      </main>

      {/* Floating System Signal */}
      <AnimatePresence>
        {isAlertOpen && (
          <motion.div
            initial={{ opacity: 0, scale: 0.8, x: 20 }}
            animate={{ opacity: 1, scale: 1, x: 0 }}
            exit={{ opacity: 0, scale: 0.8, x: 20 }}
            className="fixed bottom-24 right-6 left-6 md:left-auto md:w-80 z-50 glass-card p-4 rounded-2xl border-secondary/20 shadow-2xl"
          >
            <div className="flex gap-3">
              <div className="bg-secondary/10 p-2 rounded-lg h-fit">
                <Cpu className="text-secondary w-4 h-4 animate-pulse" />
              </div>
              <p className="text-[11px] font-mono font-bold text-secondary leading-tight uppercase tracking-tight">
                {alertContent}
              </p>
            </div>
          </motion.div>
        )}
      </AnimatePresence>

      {/* Modern Bottom Navigation */}
      <div className="fixed bottom-8 left-1/2 -translate-x-1/2 z-50 w-full max-w-sm px-6">
        <div className="bg-[#0b1326]/40 backdrop-blur-3xl border border-white/10 rounded-[2.5rem] p-2 flex justify-between items-center shadow-[0_20px_50px_rgba(0,0,0,0.6)]">
          {tabs.map((tab) => {
            const isActive = activeTab === tab.id;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id as MainTab)}
                className={`relative flex flex-col items-center justify-center flex-1 h-14 transition-all duration-300 haptic-feedback cursor-pointer ${
                  isActive ? "text-secondary" : "text-white/30"
                }`}
              >
                {isActive && (
                  <motion.div
                    layoutId="active-pill"
                    className="absolute inset-0 bg-secondary/10 border border-secondary/20 rounded-[2rem]"
                    transition={{ type: "spring", bounce: 0.3, duration: 0.6 }}
                  />
                )}
                <div className={`z-10 transition-transform duration-300 ${isActive ? "-translate-y-1 scale-110" : "scale-100"}`}>
                  <tab.icon size={22} strokeWidth={isActive ? 2.5 : 2} />
                </div>
                {isActive && (
                  <motion.span
                    initial={{ opacity: 0, y: 5 }}
                    animate={{ opacity: 1, y: 0 }}
                    className="z-10 text-[8px] font-mono font-black mt-1 uppercase tracking-widest"
                  >
                    {tab.label}
                  </motion.span>
                )}
              </button>
            );
          })}
        </div>
      </div>

    </div>
  );
}
