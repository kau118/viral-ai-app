import express from "express";
import path from "path";
import dotenv from "dotenv";
import cors from "cors";
import { createServer as createViteServer } from "vite";
import { GoogleGenerativeAI } from "@google/generative-ai";

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

// Initialize Gemini AI
let genAI: GoogleGenerativeAI | null = null;

function getGenAI(): GoogleGenerativeAI {
  if (!genAI) {
    const apiKey = process.env.GEMINI_API_KEY;
    if (!apiKey) {
      throw new Error("GEMINI_API_KEY is not configured.");
    }
    genAI = new GoogleGenerativeAI(apiKey);
  }
  return genAI;
}

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
    if (!topic) {
      return res.status(400).json({ error: "Topic is required." });
    }

    const ai = getGenAI();
    const model = ai.getGenerativeModel({ model: "gemini-pro" });

    const prompt = `Generate a complete viral content package for: "${topic}" (Vibe: ${vibe || "Professional"}).
    Format response as JSON:
    {
      "hook": "Killer opening line",
      "caption": "Engaging caption with emojis",
      "script": "30s storyboard script",
      "hashtags": ["#tag1", "#tag2", "#tag3", "#tag4", "#tag5"]
    }`;

    const result = await model.generateContent(prompt);
    const response = await result.response;
    let text = response.text();

    // Clean JSON from potential markdown formatting
    text = text.replace(/```json/g, "").replace(/```/g, "").trim();

    const data = JSON.parse(text);
    res.json(data);
  } catch (error: any) {
    console.error("AI Error:", error);
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
