import React, { useState } from "react";
import { motion, AnimatePresence } from "motion/react";
import {
  Settings,
  CreditCard,
  History,
  LogOut,
  ShieldCheck,
  ChevronRight,
  Download,
  Share2,
  Crown,
  Bell,
  Lock,
  Smartphone,
  CheckCircle2
} from "lucide-react";

export default function Profile() {
  const [toast, setToast] = useState<string | null>(null);

  const showToast = (msg: string) => {
    setToast(msg);
    setTimeout(() => setToast(null), 2500);
  };

  const handleSubscribe = () => {
    // In a real app, you would fetch order_id from your backend first
    const options = {
      key: "YOUR_RAZORPAY_KEY", // Enter your Key ID here
      amount: "49900", // Amount in paise (499.00 INR)
      currency: "INR",
      name: "Paavani AI",
      description: "Pro Master Subscription",
      handler: function (response: any) {
        showToast("Payment Successful: " + response.razorpay_payment_id);
      },
      prefill: {
        name: "Alex Rivera",
        email: "alex@paavani.ai",
      },
      theme: {
        color: "#ddb7ff",
      },
    };
    const rzp = new (window as any).Razorpay(options);
    rzp.open();
  };

  return (
    <div className="max-w-2xl mx-auto space-y-12 animate-fade-in pb-20">
      {/* Toast */}
      <AnimatePresence>
        {toast && (
          <motion.div
            initial={{ opacity: 0, y: 50, scale: 0.9 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: 30, scale: 0.9 }}
            className="fixed bottom-28 left-1/2 -translate-x-1/2 z-[100] bg-white text-black px-8 py-4 rounded-full font-black text-[10px] font-mono tracking-widest shadow-2xl flex items-center gap-3 uppercase"
          >
            <CheckCircle2 size={16} />
            {toast}
          </motion.div>
        )}
      </AnimatePresence>

      {/* Profile Header */}
      <section className="text-center space-y-8 pt-8 relative">
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_center,rgba(221,183,255,0.08)_0%,transparent_70%)] pointer-events-none" />

        <div className="relative inline-block group">
          <div className="w-40 h-40 rounded-[3.5rem] p-1.5 border-2 border-primary/20 transition-transform duration-700 group-hover:rotate-[372deg]">
            <div className="w-full h-full rounded-[3rem] overflow-hidden border-8 border-[#020617] shadow-2xl relative">
              <img
                src="https://lh3.googleusercontent.com/aida-public/AB6AXuB9CH_A3Unxpt8r-v3qDuIOwq4R7AGeES4xgNkxFVPGsIPvusHKWleESgTFqpqr8DLsj2lMzodw_oHENf7YIcmVvR2ONWJfQLn5Rg5DKsdpswgnxS5ujAx5U33khbcUkuZ6No0heNTcMrx4BQGeI2zuqXcoWcv_BDTD9u-ta92Ah43EWsLXfQm8KyfMSNBalX13ED_QuuQJJfVjo-INLmXK9-fahlGxVRefcDp-9-5o82V0gejxZ-6XjVJTx-Nb63_gvkfvYy9i01hW"
                className="w-full h-full object-cover grayscale group-hover:grayscale-0 transition-all duration-700"
                alt="Alex Rivera"
              />
            </div>
          </div>
          <div className="absolute -bottom-2 -right-2 bg-secondary p-3 rounded-2xl border-4 border-[#020617] shadow-2xl rotate-12 group-hover:rotate-0 transition-transform duration-500">
            <ShieldCheck size={24} className="text-[#003915]" />
          </div>
        </div>

        <div className="space-y-3">
          <div className="flex items-center justify-center gap-3">
             <h2 className="font-display text-4xl font-black text-white tracking-tight">Alex Rivera</h2>
             <div className="bg-primary/20 px-3 py-1 rounded-lg border border-primary/30">
                <Crown size={14} className="text-primary fill-primary" />
             </div>
          </div>
          <p className="text-white/30 text-xs font-mono tracking-[0.4em] uppercase font-black">Paavani AI Engine v2.4</p>
        </div>

        <div className="flex justify-center gap-4">
          <button className="flex items-center gap-3 px-8 py-3.5 bg-white/5 border border-white/10 rounded-[1.2rem] text-[10px] font-black font-mono tracking-widest hover:bg-white/10 transition-all haptic-feedback cursor-pointer uppercase">
            <Share2 size={16} /> Share node
          </button>
          <button className="flex items-center gap-3 px-8 py-3.5 bg-primary text-[#490080] rounded-[1.2rem] text-[10px] font-black font-mono tracking-widest shadow-[0_15px_30px_rgba(221,183,255,0.3)] haptic-feedback cursor-pointer uppercase">
            Configure
          </button>
        </div>
      </section>

      {/* Subscription Card */}
      <div className="glass-card rounded-[3rem] p-10 border-primary/20 bg-gradient-to-br from-primary/[0.08] via-transparent to-transparent flex flex-col lg:flex-row items-center gap-10 relative overflow-hidden group">
        <div className="absolute top-0 right-0 p-10 opacity-[0.03] group-hover:opacity-[0.08] transition-opacity duration-1000">
          <Crown size={180} />
        </div>

        <div className="space-y-4 relative z-10 flex-1 text-center lg:text-left">
          <span className="text-[10px] font-mono font-black text-primary uppercase tracking-[0.3em] bg-primary/10 px-4 py-1.5 rounded-full border border-primary/20">Active Subscription</span>
          <h3 className="text-4xl font-black text-white italic tracking-tight">Paavani <span className="text-primary">Master</span></h3>
          <p className="text-white/40 text-sm font-medium">Unlimited neural projections until <span className="text-white">Sept 12, 2026</span></p>
        </div>

        <button
          onClick={handleSubscribe}
          className="bg-white text-black px-10 py-5 rounded-[1.5rem] font-black text-[10px] font-mono tracking-[0.2em] uppercase haptic-feedback relative z-10 whitespace-nowrap shadow-2xl transition-all hover:scale-105 active:scale-95"
        >
          Upgrade Node
        </button>
      </div>

      {/* Settings Grid */}
      <div className="space-y-6">
        <div className="flex justify-between items-center px-6">
           <h3 className="font-mono text-[11px] font-black text-white/20 uppercase tracking-[0.4em]">Core Configuration</h3>
           <span className="text-[10px] font-mono font-black text-secondary uppercase">All systems nominal</span>
        </div>

        <div className="grid grid-cols-1 gap-4">
          <ProfileLink icon={Settings} label="Global Engine Settings" sub="Language models, output styles, webhooks" onClick={() => showToast("Settings Loaded")} />
          <ProfileLink icon={Bell} label="Alert Notifications" sub="Configure push triggers for viral spikes" onClick={() => showToast("Alerts Configured")} />
          <ProfileLink icon={History} label="Neural Export History" sub="Audit all previously generated content" rightIcon={Download} onClick={() => showToast("Fetching History")} />
          <ProfileLink icon={Smartphone} label="Device Linkage" sub="Manage connected phone nodes" onClick={() => showToast("Device Sync Active")} />
          <ProfileLink icon={Lock} label="Privacy & Security" sub="End-to-end neural encryption settings" onClick={() => showToast("Security Verified")} />
          <ProfileLink icon={LogOut} label="Deactivate Session" sub="Safely logout from current engine" color="text-red-400" onClick={() => showToast("Session Closing...")} />
        </div>
      </div>

      <div className="text-center pt-8 space-y-2">
        <p className="text-[10px] font-mono text-white/10 font-black tracking-[0.5em] uppercase">Paavani AI • Build 240.Alpha</p>
        <div className="flex justify-center gap-6 text-[9px] font-mono font-bold text-white/5 uppercase">
           <a href="#" className="hover:text-white/20 transition-colors">Legal</a>
           <a href="#" className="hover:text-white/20 transition-colors">Neural Policy</a>
           <a href="#" className="hover:text-white/20 transition-colors">API Docs</a>
        </div>
      </div>
    </div>
  );
}

function ProfileLink({ icon: Icon, label, sub, rightIcon: RightIcon = ChevronRight, color = "text-white", onClick }: any) {
  return (
    <div
      onClick={onClick}
      className="glass-card rounded-[2rem] p-7 flex items-center justify-between group cursor-pointer haptic-feedback border-white/5 hover:border-white/10 transition-all shadow-xl"
    >
      <div className="flex items-center gap-7">
        <div className="bg-white/5 p-4 rounded-[1.2rem] group-hover:bg-white/10 transition-colors shadow-2xl">
          <Icon size={24} className={color} />
        </div>
        <div>
          <h4 className={`text-base font-black tracking-tight ${color}`}>{label}</h4>
          <p className="text-[11px] text-white/30 font-bold mt-1 uppercase tracking-wider">{sub}</p>
        </div>
      </div>
      <RightIcon size={22} className="text-white/10 group-hover:text-white transition-all transform group-hover:translate-x-1" />
    </div>
  );
}
