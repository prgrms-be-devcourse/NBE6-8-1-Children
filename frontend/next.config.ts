import type { NextConfig } from "next";

const nextConfig = {
  images: {
    remotePatterns: [
      { protocol: "https", hostname: "ifh.cc" },
      { protocol: "https", hostname: "i.postimg.cc" }
    ]
  },
};

export default nextConfig;
