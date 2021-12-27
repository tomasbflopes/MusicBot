curl -n -X DELETE https://api.heroku.com/apps/j-banger-bot/dynos \
  -H "Content-Type: application/json" \
  -H "Accept: application/vnd.heroku+json; version=3" \
  -H "Authorization: Bearer ${HEROKU_TOKEN}"
