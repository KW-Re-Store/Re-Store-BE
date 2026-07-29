import { app } from "./app.js";
import { env } from "./config/env.js";

app.listen(env.PORT, () => {
  console.log(`Re-Store API listening on http://localhost:${env.PORT}`);
});
