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

// Helper for high-quality fallback content
function getSimulatedContent(topic: string, vibe: string) {
  return {
    hook: `Wait, before you scroll—did you know that most people miss the biggest viral trigger for ${topic}?`,
    caption: `Transforming your approach to ${topic} starts with one simple shift. ✨ Whether you're a pro or just starting, this is the ${vibe} strategy you need. 🚀 #PaavaniAI #Growth #Viral`,
    script: `[Scene 1]: Hook line text on screen with fast cuts. [VO]: The secret to ${topic} isn't what you think. [Scene 2]: Comparison of bad vs good strategy. [VO]: Most people do X, but the top 1% do Y. [Scene 3]: Call to action. [VO]: Follow for more neural growth hacks.`,
    hashtags: ["#PaavaniAI", "#NeuralGrowth", "#ViralStrategies", "#CreatorEconomy", `#${topic.replace(/\s+/g, '')}`],
    source: "Simulated (API Access Required)"
  };
}

app.post("/api/generate-all", async (req, res) => {
  const { topic, vibe } = req.body;
  const apiKey = process.env.GEMINI_API_KEY;

  if (!topic) return res.status(400).json({ error: "Topic is required." });

  try {
    if (!apiKey) throw new Error("API_KEY_MISSING");

    const prompt = `Generate a complete viral social media content package for: "${topic}" (Vibe: ${vibe || "Professional"}).
    Format response as valid JSON ONLY:
    {
      "hook": "string",
      "caption": "string",
      "script": "string",
      "hashtags": ["string"]
    }`;

    // Try multiple models in a fallback chain
    const models = ["gemini-1.5-flash", "gemini-pro", "gemini-1.0-pro"];
    let success = false;
    let resultData = null;

    for (const model of models) {
      try {
        const url = `https://generativelanguage.googleapis.com/v1beta/models/${model}:generateContent?key=${apiKey}`;
        const response = await fetch(url, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ contents: [{ parts: [{ text: prompt }] }] })
        });

        if (response.ok) {
          const result: any = await response.json();
          let text = result.candidates[0].content.parts[0].text;
          text = text.replace(/```json/g, "").replace(/```/g, "").trim();
          resultData = JSON.parse(text);
          success = true;
          break;
        }
      } catch (e) {
        continue;
      }
    }

    if (success && resultData) {
      res.json(resultData);
    } else {
      // If all AI models fail (404/403/Quota), use high-quality simulated fallback
      console.log("AI Models failed or unauthorized. Using smart fallback.");
      res.json(getSimulatedContent(topic, vibe));
    }

  } catch (error: any) {
    console.error("Master Error:", error.message);
    res.json(getSimulatedContent(topic, vibe));
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
