import { exec } from "child_process";
exec("ping -c 1 " + req.query.host);
