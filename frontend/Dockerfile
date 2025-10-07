# Stage 1: Build the SvelteKit app with Bun
FROM oven/bun:1 AS builder

WORKDIR /app

# Copy dependencies and install
COPY bun.lock package.json ./
RUN bun install --frozen-lockfile

# Copy the rest of the project and build
COPY . .
RUN bun run build

# Stage 2: Serve with Nginx
FROM nginx:stable-alpine

WORKDIR /usr/share/nginx/html

# Copy built SvelteKit static files
COPY --from=builder /app/build ./

COPY public/config.js ./config.js

# Copy a custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy entrypoint script
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 80

# Replace placeholders with environment variables and run
ENTRYPOINT ["/entrypoint.sh"]