/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

export type MainTab = "dashboard" | "create" | "trends" | "analytics" | "profile";

export type CreateSubTab = "hooks" | "hashtags" | "scripts" | "captions";

export interface CaptionOutput {
  caption: string;
  hashtags: string[];
}

export interface OptimizedHook {
  text: string;
  lift: string;
}

export interface OptimizeHookResponse {
  variantA: OptimizedHook;
  variantB: OptimizedHook;
}

export interface ReelHookVariation {
  hookText: string;
  retentionScore: number;
  label: string;
  tag: string;
  avgTime: string;
  viralPotential: string;
}

export interface HashtagPack {
  title: string;
  velocity: "HIGH VELOCITY" | "MEDIUM VELOCITY" | "LOW VELOCITY" | string;
  viralScore: number;
  tags: string[];
  avgReach: string;
}

export interface HashtagEngineData {
  packs: HashtagPack[];
  networkInsight: string;
}

export interface CompetitorRow {
  username: string;
  niche: string;
  followers: string;
  growth: string;
  avatar: string;
}

export interface WinningCompetitorHook {
  title: string;
  hookText: string;
  retention: string;
  views: string;
}

export interface CompetitorIntelligenceData {
  projection: string;
  blueprint: string;
  winningHooks: WinningCompetitorHook[];
}
