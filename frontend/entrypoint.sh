#!/bin/sh
set -e

# Replace placeholder in config.js with env var
sed -i "s|%%API_BASE_URL%%|${API_BASE_URL}|g" /usr/share/nginx/html/config.js

# Inject <script src="/config.js"></script> into index.html
if ! grep -q '<script src="/config.js"></script>' /usr/share/nginx/html/index.html; then
    # Insert before closing </head> tag
    sed -i '/<\/head>/i <script src="/config.js"></script>' /usr/share/nginx/html/index.html
fi

# Start Nginx
exec nginx -g "daemon off;"
