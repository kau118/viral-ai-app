import express from "express";
import path from "path";
import dotenv from "dotenv";
import cors from "cors";
import { createServer as createViteServer } from "vite";
import { GoogleGenAI, Type } from "@google/genai";

dotenv.config();

const app = express();

// Very permissive CORS for mobile WebViews
app.use(cors({
  origin: '*',
  methods: ['GET', 'POST', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization', 'Accept'],
  credentials: true
}));

app.use(express.json());

const PORT = process.env.PORT || 3000;

// Shared Gemini AI lazy-initialization
let aiClient: GoogleGenAI | null = null;

function getAIClient(): GoogleGenAI {
  if (!aiClient) {
    const apiKey = process.env.GEMINI_API_KEY;
    if (!apiKey) {
      throw new Error("GEMINI_API_KEY environment variable is not configured. Please add it in the Secrets panel.");
    }
    aiClient = new GoogleGenAI({
      apiKey,
      httpOptions: {
        headers: {
          "User-Agent": "aistudio-build",
        },
      },
    });
  }
  return aiClient;
}

// Ensure the server starts gracefully even without API keys
app.get("/api/health", (req, res) => {
  const hasKey = !!process.env.GEMINI_API_KEY;
  res.json({ status: "ok", geminiKeyAvailable: hasKey });
});

// LOGIN ENDPOINT (Verification placeholder)
app.post("/api/login", (req, res) => {
  const { email, password } = req.body;
  if (email && password) {
    res.json({ status: "success", user: { email, id: "vai_user_1" } });
  } else {
    res.status(400).json({ error: "Email and password required" });
  }
});

// 1. SMART CAPTION GENERATOR ENDPOINT
app.post("/api/generate-caption", async (req, res) => {
  try {
    const { topic, tone } = req.body;
    if (!topic) {
      return res.status(400).json({ error: "Content topic/niche is required." });
    }

    const ai = getAIClient();
    const prompt = `You are an elite social media copywriter and growth hacker.
Generate a highly engaging, high-retention viral social media caption based on the following input:
Niche/Topic: "${topic}"
Tone: "${tone || "Professional"}"

The output MUST contain:
- A killer hook sentence at the start
- A compelling main message with active spacing and smart emojis (max 3 emojis)
- A clear, subtle Call to Action (CTA) or question to boost comments
- Exactly 3 targeted high-conversion hashtags based on this niche.

Return the response in JSON format matching the schema:
{
  "caption": "the caption text including hook and body copy with emojis",
  "hashtags": ["#tag1", "#tag2", "#tag3"]
}`;

    const response = await ai.models.generateContent({
      model: "gemini-1.5-flash",
      contents: prompt,
      config: {
        responseMimeType: "application/json",
        responseSchema: {
          type: Type.OBJECT,
          properties: {
            caption: { type: Type.STRING },
            hashtags: {
              type: Type.ARRAY,
              items: { type: Type.STRING },
            },
          },
          required: ["caption", "hashtags"],
        },
      },
    });

    const data = JSON.parse(response.text || "{}");
    res.json(data);
  } catch (error: any) {
    console.error("Generate Caption Error:", error);
    res.status(500).json({ error: error.message || "Failed to generate caption." });
  }
});

// 2. HOOK OPTIMIZATION ENDPOINT
app.post("/api/optimize-hook", async (req, res) => {
  try {
    const { draftHook } = req.body;
    if (!draftHook) {
      return res.status(400).json({ error: "Draft hook is required." });
    }

    const ai = getAIClient();
    const prompt = `You are a viral hook optimization engine. Analyze this draft social media hook:
"${draftHook}"

Generate exactly TWO highly optimized, high-retention variations:
- Variant A: Focuses on curiosity gap, shocking truth, or contrarian angle. Est. lift: +40% to +50%.
- Variant B: Focuses on quick utility, secret blueprint, or instant gratification. Est. lift: +25% to +35%.

Return the response in JSON format matching the schema:
{
  "variantA": {
    "text": "the exact optimized hook quote",
    "lift": "+45% Est."
  },
  "variantB": {
    "text": "the exact optimized hook quote",
    "lift": "+32% Est."
  }
}`;

    const response = await ai.models.generateContent({
      model: "gemini-1.5-flash",
      contents: prompt,
      config: {
        responseMimeType: "application/json",
        responseSchema: {
          type: Type.OBJECT,
          properties: {
            variantA: {
              type: Type.OBJECT,
              properties: {
                text: { type: Type.STRING },
                lift: { type: Type.STRING },
              },
              required: ["text", "lift"],
            },
            variantB: {
              type: Type.OBJECT,
              properties: {
                text: { type: Type.STRING },
                lift: { type: Type.STRING },
              },
              required: ["text", "lift"],
            },
          },
          required: ["variantA", "variantB"],
        },
      },
    });

    const data = JSON.parse(response.text || "{}");
    res.json(data);
  } catch (error: any) {
    console.error("Optimize Hook Error:", error);
    res.status(500).json({ error: error.message || "Failed to optimize hook." });
  }
});

// 3. REEL HOOK GENERATOR ENDPOINT
app.post("/api/generate-hooks", async (req, res) => {
  try {
    const { topic, vibe } = req.body;
    if (!topic) {
      return res.status(400).json({ error: "Topic/concept is required." });
    }

    const ai = getAIClient();
    const prompt = `You are an AI video growth consultant analyzing 10k+ trending short-form reels.
Generate exactly 3 diverse, ultra-clickable video hooks based on:
Concept: "${topic}"
Vibe: "${vibe || "Educational"}"

The generated hooks must be tailored specifically to short-form loops (TikTok, Instagram Reels, YouTube Shorts).
Each variation should have:
1. "hookText" (the exact text to say or display on screen, e.g. "Pov: You finally hit...")
2. "retentionScore" (an integer from 50 to 99, representing predicted retention)
3. "label" (e.g. "HIGH RETENTION", "TREND SETTER", "ENGAGEMENT BOMB")
4. "tag" (e.g. "#Educational", "#Funny", "#Dramatic")
5. "avgTime" (e.g. "3.2s Average")
6. "viralPotential" (e.g. "Extreme", "High", "Critical")

Return the response in JSON format matching the schema:
{
  "variations": [
    {
      "hookText": "string",
      "retentionScore": 92,
      "label": "string",
      "tag": "string",
      "avgTime": "string",
      "viralPotential": "string"
    }
  ]
}`;

    const response = await ai.models.generateContent({
      model: "gemini-1.5-flash",
      contents: prompt,
      config: {
        responseMimeType: "application/json",
        responseSchema: {
          type: Type.OBJECT,
          properties: {
            variations: {
              type: Type.ARRAY,
              items: {
                type: Type.OBJECT,
                properties: {
                  hookText: { type: Type.STRING },
                  retentionScore: { type: Type.INTEGER },
                  label: { type: Type.STRING },
                  tag: { type: Type.STRING },
                  avgTime: { type: Type.STRING },
                  viralPotential: { type: Type.STRING },
                },
                required: ["hookText", "retentionScore", "label", "tag", "avgTime", "viralPotential"],
              },
            },
          },
          required: ["variations"],
        },
      },
    });

    const data = JSON.parse(response.text || "{}");
    res.json(data);
  } catch (error: any) {
    console.error("Generate Hooks Error:", error);
    res.status(500).json({ error: error.message || "Failed to generate reel hooks." });
  }
});

// 4. HASHTAG ENGINE ENDPOINT
app.post("/api/generate-hashtags", async (req, res) => {
  try {
    const { keywords } = req.body;
    if (!keywords) {
      return res.status(400).json({ error: "Keywords are required." });
    }

    const ai = getAIClient();
    const prompt = `You are a social graph analyzer. Generate a professional trending hashtag cluster based on:
Keywords/Niche: "${keywords}"

Generate exactly 3 thematic hashtag packs:
1. "Creative AI Pack" / High velocity (or customized theme relevant to keywords)
2. "Tech Founders" / Medium velocity (or customized theme relevant to keywords)
3. "Minimal Setup" / Low velocity/Niche specific (or customized theme relevant to keywords)

Each pack must have a list of exactly 4-5 hashtags, a customized title, velocity description, and a predicted viral score percentage.
Also provide a "networkInsight" paragraph summarizing the strategic opportunity.

Return the response in JSON format matching the schema:
{
  "packs": [
    {
      "title": "string",
      "velocity": "HIGH VELOCITY" | "MEDIUM VELOCITY" | "LOW VELOCITY",
      "viralScore": 94,
      "tags": ["#tag1", "#tag2", "#tag3", "#tag4", "#tag5"],
      "avgReach": "string"
    }
  ],
  "networkInsight": "string"
}`;

    const response = await ai.models.generateContent({
      model: "gemini-1.5-flash",
      contents: prompt,
      config: {
        responseMimeType: "application/json",
        responseSchema: {
          type: Type.OBJECT,
          properties: {
            packs: {
              type: Type.ARRAY,
              items: {
                type: Type.OBJECT,
                properties: {
                  title: { type: Type.STRING },
                  velocity: { type: Type.STRING },
                  viralScore: { type: Type.INTEGER },
                  tags: {
                    type: Type.ARRAY,
                    items: { type: Type.STRING },
                  },
                  avgReach: { type: Type.STRING },
                },
                required: ["title", "velocity", "viralScore", "tags", "avgReach"],
              },
            },
            networkInsight: { type: Type.STRING },
          },
          required: ["packs", "networkInsight"],
        },
      },
    });

    const data = JSON.parse(response.text || "{}");
    res.json(data);
  } catch (error: any) {
    console.error("Generate Hashtags Error:", error);
    res.status(500).json({ error: error.message || "Failed to generate hashtags." });
  }
});

// 5. COMPETITOR REBUTTAL/ROADMAP ENDPOINT
app.post("/api/generate-rebuttal", async (req, res) => {
  try {
    const { competitor, metric } = req.body;
    
    const ai = getAIClient();
    const prompt = `You are an elite competitive intelligence marketing bot for creators.
Formulate a highly strategic, offensive growth roadmap or content rebuttal to completely outpace the competitor:
Competitor Username: "${competitor || "@pixel_ninja"}"
Competitor's Velocity: "${metric || "14.2% growth / Motion Design"}"

Generate:
1. A potential reach gain projection (e.g., "~2.4M potential reach gain")
2. An actionable algorithmic "alpha blueprint" to counter their strategy.
3. Three simulated winning hooks in their style with 80%+ retention targets.

Return the response in JSON format matching the schema:
{
  "projection": "string",
  "blueprint": "string",
  "winningHooks": [
    {
      "title": "string",
      "hookText": "string",
      "retention": "98% RETENTION",
      "views": "2.4M Views"
    }
  ]
}`;

    const response = await ai.models.generateContent({
      model: "gemini-1.5-flash",
      contents: prompt,
      config: {
        responseMimeType: "application/json",
        responseSchema: {
          type: Type.OBJECT,
          properties: {
            projection: { type: Type.STRING },
            blueprint: { type: Type.STRING },
            winningHooks: {
              type: Type.ARRAY,
              items: {
                type: Type.OBJECT,
                properties: {
                  title: { type: Type.STRING },
                  hookText: { type: Type.STRING },
                  retention: { type: Type.STRING },
                  views: { type: Type.STRING },
                },
                required: ["title", "hookText", "retention", "views"],
              },
            },
          },
          required: ["projection", "blueprint", "winningHooks"],
        },
      },
    });

    const data = JSON.parse(response.text || "{}");
    res.json(data);
  } catch (error: any) {
    console.error("Generate Rebuttal Error:", error);
    res.status(500).json({ error: error.message || "Failed to generate rebuttal." });
  }
});

// 6. MASTER VIRAL CONTENT GENERATOR (Everything in one)
app.post("/api/generate-all", async (req, res) => {
  try {
    const { topic, vibe } = req.body;
    if (!topic) {
      return res.status(400).json({ error: "Topic is required." });
    }

    const ai = getAIClient();
    const prompt = `You are an elite AI Social Media Growth Assistant.
Generate a complete viral content package for the following topic:
Topic: "${topic}"
Vibe: "${vibe || "Professional"}"

The package MUST include:
1. "hook": A high-retention opening line (first 3 seconds).
2. "caption": A compelling, spaced-out caption with max 3 emojis.
3. "script": A 30-60 second storyboard/script including visual cues and voiceover.
4. "hashtags": Exactly 5 trending, high-conversion hashtags.

Return the response in JSON format matching the schema:
{
  "hook": "string",
  "caption": "string",
  "script": "string",
  "hashtags": ["#tag1", "#tag2", "#tag3", "#tag4", "#tag5"]
}`;

    const response = await ai.models.generateContent({
      model: "gemini-1.5-flash",
      contents: prompt,
      config: {
        responseMimeType: "application/json",
        responseSchema: {
          type: Type.OBJECT,
          properties: {
            hook: { type: Type.STRING },
            caption: { type: Type.STRING },
            script: { type: Type.STRING },
            hashtags: {
              type: Type.ARRAY,
              items: { type: Type.STRING }
            }
          },
          required: ["hook", "caption", "script", "hashtags"],
        },
      },
    });

    const data = JSON.parse(response.text || "{}");
    res.json(data);
  } catch (error: any) {
    console.error("Generate All Error:", error);
    res.status(500).json({
      error: "Failed to generate package.",
      details: error.message,
      stack: process.env.NODE_ENV === 'development' ? error.stack : undefined
    });
  }
});

// VITE MIDDLEWARE SETUP & STATIC HANDLING
async function startServer() {
  if (process.env.NODE_ENV !== "production") {
    // Development Mode
    const vite = await createViteServer({
      server: { middlewareMode: true },
      appType: "spa",
    });
    app.use(vite.middlewares);
  } else {
    // Production Mode
    const distPath = path.join(process.cwd(), "dist");
    app.use(express.static(distPath));
    app.get("*", (req, res) => {
      res.sendFile(path.join(distPath, "index.html"));
    });
  }

  app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server running on http://0.0.0.0:${PORT} in ${process.env.NODE_ENV || "development"} mode`);
  });
}

startServer();
