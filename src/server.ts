import { createServer } from "http";
import * as express from "express";
import * as session from "express-session";
import * as cors from "cors";
import { urlencoded, json } from "body-parser";
import * as passport from "passport";
import { Strategy } from "passport-discord";

import { TrafficLogger } from "./middleware";

import { HOSTNAME, PORT, SESSION_SECRET, DISCORD_CLIENT_ID, DISCORD_CLIENT_SECRET } from "../config";

import { initDb } from "./repository/mongo.repository";

initDb(async () => {
    const app = express();

    // Apply dependency middleware
    app.use(cors());
    app.use(urlencoded({ extended: true }));
    app.use(json());

    // Apply my own middleware
    app.use(TrafficLogger);

    // Apply user login middleware
    app.use(session({ secret: SESSION_SECRET, resave: true, saveUninitialized: true }))
    app.use(passport.initialize());
    app.use(passport.session());

    // Define Discord user login middleware
    passport.use(new Strategy({
        clientID: DISCORD_CLIENT_ID,
        clientSecret: DISCORD_CLIENT_SECRET,
        callbackURL: `${HOSTNAME}:${PORT}/api/v1/auth/discord/redirect`, // TODO replace localhost with normal hostname (but how do I reference it, if not literally?)
        scope: ["identify"]
    },
        (accessToken, refreshToken, profile, cb) => {
            console.log(accessToken, refreshToken);
            console.log(JSON.stringify(profile));
            return cb();
            // TODO: Make a User Service that will take the profile information and save it via the User Model!
            // User.findOrCreate({ discordId: profile.id }, function (err, user) {
            //     return cb(err, user);
            // });
        }));

    // Load routers (requires DB connection to import)
    const CoreRouter = await import("./routes");
    const APIRouter = await import("./routes/api");

    // Apply routes
    app.use("/", CoreRouter.default);
    app.use("/api/v1", APIRouter.default);

    createServer(app)
        .listen(PORT || 3000, () => console.log(`\x1b[35m** \x1b[0mListening on localhost:\x1b[32m${PORT || 3000}\x1b[0m`));
}).catch(err => console.error(err));