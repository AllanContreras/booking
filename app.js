const express = require('express');
const path = require('path');
const bodyParser = require('body-parser');
const bcrypt = require('bcrypt');
const mongoose = require('mongoose');
const User = require('./models/User');

const app = express();

// Middleware (MOVER AQUÍ)
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

// Servir archivos estáticos
app.use(express.static(path.join(__dirname, 'public')));

// URI de conexión a MongoDB
const mongo_uri = 'mongodb+srv://davidscastro29:Clave123!@cluster0.0tx8p.mongodb.net/booking?retryWrites=true&w=majority&appName=Cluster0';

// Conectar a MongoDB
mongoose.connect(mongo_uri, { useNewUrlParser: true, useUnifiedTopology: true })
    .then(() => console.log(`✅ Conectado a la base de datos: ${mongo_uri}`))
    .catch(err => console.error(`❌ Error al conectar a MongoDB: ${err.message}`));

// **Ruta para obtener usuarios**
app.get('/users', async (req, res) => {
    try {
        const users = await User.find();  // Consulta a MongoDB
        res.status(200).json(users);
    } catch (error) {
        res.status(500).json({ message: 'Error al obtener usuarios', error });
    }
});

// **Ruta para autenticar usuarios**
app.post('/authenticate', async (req, res) => {
    try {
        console.log(req.body);  // Verificar datos recibidos en consola
        const { email, password } = req.body;

        // Buscar usuario en la base de datos
        const user = await User.findOne({ email });

        if (!user) {
            return res.status(404).send('EL USUARIO NO EXISTE');
        }

        // Verificar la contraseña
        const result = await user.isCorrectPassword(password);

        if (result) {
            res.status(200).send('USUARIO AUTENTICADO CORRECTAMENTE');
        } else {
            res.status(401).send('USUARIO Y/O CONTRASEÑA INCORRECTA');
        }

    } catch (err) {
        res.status(500).send('ERROR AL AUTENTICAR AL USUARIO');
    }
});

// Iniciar el servidor
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`🔥 Servidor ejecutándose en http://localhost:${PORT}`);
});

module.exports = app;
