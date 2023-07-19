import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import NavBar from "../components/NavBar";
import {appConfig} from "../config";
import {useContext} from "react";
import Context from "../context/context";
import {useNavigate} from "react-router-dom";

function Copyright(props) {
    return (
        <Typography variant="body2" color="text.secondary" align="center" {...props}>
            {'Copyright © '}
            <Link color="inherit" href="https://mui.com/">
                Your Website
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

const theme = createTheme();

export default function Change_Password_User() {

    const ctx = useContext(Context)
    const navigate = useNavigate()

    const handleSubmit = (event) => {
        ctx.checkToken()
        event.preventDefault();

        const data = new FormData(event.currentTarget);

        changePassword({
            password: data.get('oldpassword'),
            newpassword: data.get('newpassword'),
        }).then(()=>{});
    };

    const changePassword = async (data) => {
        try {
            const response = await fetch(`${appConfig.server}/auth/changePasswordUser`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + ctx.authToken
                },
                body: JSON.stringify({
                    password: data.password,
                    newpassword: data.newpassword,
                }),
            });
            const resp = await response.json();

            if (response.ok) {
                alert(resp.message)
                ctx.logout()
                navigate("/")
            } else {
                alert(resp.message);
            }

        } catch (e) {
            alert("Connection lost");
        }
    };

    return (
        <Box>
            <NavBar/>
            <Container className="blackout" component="main" maxWidth="xs">

                <Box
                    sx={{
                        marginTop: 2,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >

                    <Typography component="h1" variant="h4">
                        Zmień Hasło:
                    </Typography>
                    <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            type = "password"
                            id="oldpassword"
                            label="Stare Hasło"
                            name="oldpassword"
                            autoComplete="oldpassword"

                        />
                        <TextField
                            margin="normal"
                            required
                            type = "password"
                            fullWidth
                            name="newpassword"
                            label="Nowe Hasło"
                            id="newpassword"

                        />

                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}

                        >
                            Zmień hasło
                        </Button>

                    </Box>
                </Box>
            </Container>
        </Box>

    );
}