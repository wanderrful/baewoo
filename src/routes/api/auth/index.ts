import { authenticate } from "passport";
import { Router } from "express";

const AuthRouter = Router();

// Turns out Twitch needs a phone number to register an app for its OAuth2 services, so let's do Discord
AuthRouter.get("/discord", authenticate("discord"));

// https://discordapp.com/api/oauth2/authorize?client_id=631681259155750913&redirect_uri=https%3A%2F%2Flocalhost%3A4000%2Fapi%2Fv1%2Fauth%2Fdiscord%2Fredirect&response_type=code&scope=identify
AuthRouter.get("/discord/redirect", authenticate("discord", { failureRedirect: "/" }), (req, res) => {
    // Successful authentication, redirect to home.
    res.redirect("/");
});

export default AuthRouter;