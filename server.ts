import express from "express";
import path from "path";
import dotenv from "dotenv";
import cors from "cors";
import { createServer as createViteServer } from "vite";

dotenv.config();

const app = express();

app.use(cors({
  origin: '*',
  methods: ['GET', 'POST', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization', 'Accept'],
  credentials: true
}));

app.use(express.json());

const PORT = process.env.PORT || 3000;

app.get("/api/health", (req, res) => {
  const hasKey = !!process.env.GEMINI_API_KEY;
  res.json({ status: "ok", geminiKeyAvailable: hasKey });
});

app.post("/api/login", (req, res) => {
  const { email, password } = req.body;
  if (email && password) {
    res.json({ status: "success", user: { email, id: "vai_user_1" } });
  } else {
    res.status(400).json({ error: "Email and password required" });
  }
});

app.post("/api/generate-all", async (req, res) => {
  try {
    const { topic, vibe } = req.body;
    const apiKey = process.env.GEMINI_API_KEY;

    if (!topic) return res.status(400).json({ error: "Topic is required." });
    if (!apiKey) return res.status(500).json({ error: "API Key missing." });

    const prompt = `Generate a complete viral social media content package for: "${topic}" (Vibe: ${vibe || "Professional"}).
    Format response as valid JSON:
    {
      "hook": "Killer opening line",
      "caption": "Engaging caption with emojis",
      "script": "30s storyboard script",
      "hashtags": ["#tag1", "#tag2", "#tag3", "#tag4", "#tag5"]
    }`;

    // Directly calling REST API v1beta
    const url = `https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${apiKey}`;

    const response = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        contents: [{ parts: [{ text: prompt }] }]
      })
    });

    const result: any = await response.json();

    if (!response.ok) {
      console.error("Gemini API Error:", result);
      throw new Error(result.error?.message || "Gemini API failure");
    }

    let text = result.candidates[0].content.parts[0].text;
    text = text.replace(/```json/g, "").replace(/```/g, "").trim();

    const data = JSON.parse(text);
    res.json(data);

  } catch (error: any) {
    console.error("AI Error:", error.message);
    res.status(500).json({
      error: "AI Generation Failed",
      details: error.message
    });
  }
});

// VITE MIDDLEWARE SETUP & STATIC HANDLING
async function startServer() {
  if (process.env.NODE_ENV !== "production") {
    const vite = await createViteServer({
      server: { middlewareMode: true },
      appType: "spa",
    });
    app.use(vite.middlewares);
  } else {
    const distPath = path.join(process.cwd(), "dist");
    app.use(express.static(distPath));
    app.get("*", (req, res) => {
      res.sendFile(path.join(distPath, "index.html"));
    });
  }

  app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server running on port ${PORT}`);
  });
}

startServer();
