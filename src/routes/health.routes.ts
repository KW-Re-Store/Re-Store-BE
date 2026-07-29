import { Router } from "express";

export const healthRouter = Router();

healthRouter.get("/", (_request, response) => {
  response.json({
    status: "ok",
    message: "Re-Store API is running.",
    timestamp: new Date().toISOString()
  });
});
