# Build
FROM node:20-alpine AS build
WORKDIR /app
COPY . .
RUN npm install -g @quasar/cli && npm install && quasar build

# Serve
FROM nginx:alpine
COPY --from=build /app/dist/spa /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
