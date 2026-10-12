import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// strictPort: se a porta 5173 estiver ocupada, o Vite avisa em vez de abrir em outra porta.
// Isso evita o erro de CORS, porque o backend só aceita o front em http://localhost:5173 por padrão.
export default defineConfig({
  plugins: [react()],
  server: { port: 5173, strictPort: true },
});
