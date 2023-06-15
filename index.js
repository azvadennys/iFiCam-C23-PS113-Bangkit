// index.js
const express = require('express');
const app = express();
const PORT = process.env.PORT || 8080;
const serviceAccount = require('./capstone-ificam-firebase-adminsdk-vu20y-f0cb3a78ba.json');
const admin = require('firebase-admin');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});

app.use(express.json());
app.use('/api/users', require('./routes/users'));
app.use('/api/stories', require('./routes/stories'));
app.use('/api/histories', require('./routes/histories'));
app.use('/api/predicts', require('./routes/predicts'));
app.use('/api/fishs', require('./routes/fishs'));
app.use('/api/recipes', require('./routes/recipes'))
app.use('/api/articles', require('./routes/articles'))

app.use('/', (req, res) => {
    return res.status(200)
    .json({
        'message':'Welcome to iFishCam Rest API'
    });
});

app.use((req, res) => {
    return res.status(404)
    .json({
        'message':'404 Endpoint Not Found'
    });
});

app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
