const id = Number(req.query.id);
db.query("SELECT * FROM users WHERE id = $1", [id]);
