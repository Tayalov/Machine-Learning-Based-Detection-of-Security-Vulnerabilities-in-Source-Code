const id = req.query.id;
const sql = "SELECT * FROM users WHERE id = " + id;
db.query(sql);
