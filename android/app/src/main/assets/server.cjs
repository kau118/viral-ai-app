var __create = Object.create;
var __defProp = Object.defineProperty;
var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
var __getOwnPropNames = Object.getOwnPropertyNames;
var __getProtoOf = Object.getPrototypeOf;
var __hasOwnProp = Object.prototype.hasOwnProperty;
var __copyProps = (to, from, except, desc) => {
  if (from && typeof from === "object" || typeof from === "function") {
    for (let key of __getOwnPropNames(from))
      if (!__hasOwnProp.call(to, key) && key !== except)
        __defProp(to, key, { get: () => from[key], enumerable: !(desc = __getOwnPropDesc(from, key)) || desc.enumerable });
  }
  return to;
};
var __toESM = (mod, isNodeMode, target) => (target = mod != null ? __create(__getProtoOf(mod)) : {}, __copyProps(
  // If the importer is in node compatibility mode or this is not an ESM
  // file that has been converted to a CommonJS file using a Babel-
  // compatible transform (i.e. "__esModule" has not been set), then set
  // "default" to the CommonJS "module.exports" for node compatibility.
  isNodeMode || !mod || !mod.__esModule ? __defProp(target, "default", { value: mod, enumerable: true }) : target,
  mod
));

// server.ts
var import_express = __toESM(require("express"), 1);
var import_path = __toESM(require("path"), 1);
var import_dotenv = __toESM(require("dotenv"), 1);
var import_cors = __toESM(require("cors"), 1);
var import_vite = require("vite");
var import_genai = require("@google/genai");
import_dotenv.default.config();
var app = (0, import_express.default)();
app.use((0, import_cors.default)({
  origin: "*",
  methods: ["GET", "POST", "OPTIONS"],
  allowedHeaders: ["Content-Type", "Authorization", "Accept"],
  credentials: true
}));
app.use(import_express.default.json());
var PORT = process.env.PORT || 3e3;
var aiClient = null;
function getAIClient() {
  if (!aiClient) {
    const apiKey = process.env.GEMINI_API_KEY;
    if (!apiKey) {
      throw new Error("GEMINI_API_KEY environment variable is not configured. Please add it in the Secrets panel.");
    }
    aiClient = new import_genai.GoogleGenAI({
      apiKey,
      httpOptions: {
        headers: {
          "User-Agent": "aistudio-build"
        }
      }
    });
  }
  return aiClient;
}
app.get("/api/health", (req, res) => {
  const hasKey = !!process.env.GEMINI_API_KEY;
  res.json({ status: "ok", geminiKeyAvailable: hasKey });
});
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
          type: import_genai.Type.OBJECT,
          properties: {
            caption: { type: import_genai.Type.STRING },
            hashtags: {
              type: import_genai.Type.ARRAY,
              items: { type: import_genai.Type.STRING }
            }
          },
          required: ["caption", "hashtags"]
        }
      }
    });
    const data = JSON.parse(response.text || "{}");
    res.json(data);
  } catch (error) {
    console.error("Generate Caption Error:", error);
    res.status(500).json({ error: error.message || "Failed to generate caption." });
  }
});
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
          type: import_genai.Type.OBJECT,
          properties: {
            variantA: {
              type: import_genai.Type.OBJECT,
              properties: {
                text: { type: import_genai.Type.STRING },
                lift: { type: import_genai.Type.STRING }
              },
              required: ["text", "lift"]
            },
            variantB: {
              type: import_genai.Type.OBJECT,
              properties: {
                text: { type: import_genai.Type.STRING },
                lift: { type: import_genai.Type.STRING }
              },
              required: ["text", "lift"]
            }
          },
          required: ["variantA", "variantB"]
        }
      }
    });
    const data = JSON.parse(response.text || "{}");
    res.json(data);
  } catch (error) {
    console.error("Optimize Hook Error:", error);
    res.status(500).json({ error: error.message || "Failed to optimize hook." });
  }
});
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
          type: import_genai.Type.OBJECT,
          properties: {
            variations: {
              type: import_genai.Type.ARRAY,
              items: {
                type: import_genai.Type.OBJECT,
                properties: {
                  hookText: { type: import_genai.Type.STRING },
                  retentionScore: { type: import_genai.Type.INTEGER },
                  label: { type: import_genai.Type.STRING },
                  tag: { type: import_genai.Type.STRING },
                  avgTime: { type: import_genai.Type.STRING },
                  viralPotential: { type: import_genai.Type.STRING }
                },
                required: ["hookText", "retentionScore", "label", "tag", "avgTime", "viralPotential"]
              }
            }
          },
          required: ["variations"]
        }
      }
    });
    const data = JSON.parse(response.text || "{}");
    res.json(data);
  } catch (error) {
    console.error("Generate Hooks Error:", error);
    res.status(500).json({ error: error.message || "Failed to generate reel hooks." });
  }
});
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
          type: import_genai.Type.OBJECT,
          properties: {
            packs: {
              type: import_genai.Type.ARRAY,
              items: {
                type: import_genai.Type.OBJECT,
                properties: {
                  title: { type: import_genai.Type.STRING },
                  velocity: { type: import_genai.Type.STRING },
                  viralScore: { type: import_genai.Type.INTEGER },
                  tags: {
                    type: import_genai.Type.ARRAY,
                    items: { type: import_genai.Type.STRING }
                  },
                  avgReach: { type: import_genai.Type.STRING }
                },
                required: ["title", "velocity", "viralScore", "tags", "avgReach"]
              }
            },
            networkInsight: { type: import_genai.Type.STRING }
          },
          required: ["packs", "networkInsight"]
        }
      }
    });
    const data = JSON.parse(response.text || "{}");
    res.json(data);
  } catch (error) {
    console.error("Generate Hashtags Error:", error);
    res.status(500).json({ error: error.message || "Failed to generate hashtags." });
  }
});
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
          type: import_genai.Type.OBJECT,
          properties: {
            projection: { type: import_genai.Type.STRING },
            blueprint: { type: import_genai.Type.STRING },
            winningHooks: {
              type: import_genai.Type.ARRAY,
              items: {
                type: import_genai.Type.OBJECT,
                properties: {
                  title: { type: import_genai.Type.STRING },
                  hookText: { type: import_genai.Type.STRING },
                  retention: { type: import_genai.Type.STRING },
                  views: { type: import_genai.Type.STRING }
                },
                required: ["title", "hookText", "retention", "views"]
              }
            }
          },
          required: ["projection", "blueprint", "winningHooks"]
        }
      }
    });
    const data = JSON.parse(response.text || "{}");
    res.json(data);
  } catch (error) {
    console.error("Generate Rebuttal Error:", error);
    res.status(500).json({ error: error.message || "Failed to generate rebuttal." });
  }
});
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
          type: import_genai.Type.OBJECT,
          properties: {
            hook: { type: import_genai.Type.STRING },
            caption: { type: import_genai.Type.STRING },
            script: { type: import_genai.Type.STRING },
            hashtags: {
              type: import_genai.Type.ARRAY,
              items: { type: import_genai.Type.STRING }
            }
          },
          required: ["hook", "caption", "script", "hashtags"]
        }
      }
    });
    const data = JSON.parse(response.text || "{}");
    res.json(data);
  } catch (error) {
    console.error("Generate All Error:", error);
    res.status(500).json({ error: error.message || "Failed to generate package." });
  }
});
async function startServer() {
  if (process.env.NODE_ENV !== "production") {
    const vite = await (0, import_vite.createServer)({
      server: { middlewareMode: true },
      appType: "spa"
    });
    app.use(vite.middlewares);
  } else {
    const distPath = import_path.default.join(process.cwd(), "dist");
    app.use(import_express.default.static(distPath));
    app.get("*", (req, res) => {
      res.sendFile(import_path.default.join(distPath, "index.html"));
    });
  }
  app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server running on http://0.0.0.0:${PORT} in ${process.env.NODE_ENV || "development"} mode`);
  });
}
startServer();
//# sourceMappingURL=server.cjs.map
