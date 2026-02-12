const a = Number(req.body.a);
const b = Number(req.body.b);

if (!Number.isFinite(a) || !Number.isFinite(b)) {
  return res.status(400).send("Invalid input");
}

const result = a + b;
res.send(String(result));
