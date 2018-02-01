package com.example.app.Routes

import com.example.app.{AuthenticationSupport, CookieStrategy, SlickRoutes}
import com.example.app.models._
import org.json4s.JsonAST.JObject

trait AppRoutes extends SlickRoutes with AuthenticationSupport{


  get("/") {
    val authenticated = new CookieStrategy(this).checkAuthentication()
    val registered = authenticated.map(_.registered).getOrElse(false)

    <html>
      <head>
        <link rel="stylesheet" href="/front-end/dist/main.css" />
        </head>
        <body>
          <div id="app"></div>
          <script>
            var CONFIG = new Object();
            CONFIG.auth = {authenticated.isDefined};
            CONFIG.registered = {registered};
          </script>
          <script src="/front-end/dist/bundle.js"></script>
        </body>
      </html>
  }

  get("/.well-known/acme-challenge/wwj7_foOCQxB8G4sacJFZ7XZnjapPBoDn0p8BRMvbBg") {
    "wwj7_foOCQxB8G4sacJFZ7XZnjapPBoDn0p8BRMvbBg.HWkwREtv33D2TmZYw0bDSKTUQ8Pg9AqpQXfm2ta0XwM"
  }

}
