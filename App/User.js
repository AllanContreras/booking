const mongoose = require('mongoose');
const bcrypt = require('bcrypt');
const saltRounds = 10;

const UserSchema = new mongoose.Schema({
    name: { type: String, required: true },
    email: { type: String, required: true, unique: true },
    role: { type: String, enum: ['ADMIN', 'USER'], required: true },
    password: { type: String, required: true }
});

UserSchema.pre('save', function (next) {
    if (this.isNew || this.isModified('password')) {
        bcrypt.hash(this.password, saltRounds, (err, hashedPassword) => {
            if (err) return next(err);
            this.password = hashedPassword;
            next();
        });
    } else {
        next();
    }
});

UserSchema.methods.isCorrectPassword = async function (password) {
    return await bcrypt.compare(password, this.password);
};

const User = mongoose.model('User', UserSchema);
module.exports = User;
