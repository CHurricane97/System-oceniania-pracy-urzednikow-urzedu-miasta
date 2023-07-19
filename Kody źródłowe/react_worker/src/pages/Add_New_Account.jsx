import * as React from 'react';

import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';

import Box from '@mui/material/Box';

import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import NavBar from "../components/NavBar";

import SelectAccountType from "../components/SelectAccountType";
import {useContext, useState} from "react";
import {appConfig} from "../config";
import Context from "../context/context";
import {useNavigate} from "react-router-dom";

export default function Add_New_Account() {

    const [value, setValue] = useState('ROLE_USER')
    const ctx = useContext(Context)
    const navigate = useNavigate()

    const addNewAccount = async (data) => {
        const url = `${appConfig.server}/auth/register`;
        try {
            const response = await fetch(`${url}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + ctx.authToken
                },
                body: JSON.stringify({
                    login: data.login,
                    password: data.password,
                    pesel: data.pesel,
                    role: data.role
                }),
            });
            const resp = await response.json();

            alert(resp.message);

            if (response.ok) {
                navigate("/menuWorker")
            }

        } catch (e) {
            alert("Connection lost");
        }
    };

    const handleSubmit = (event) => {

        ctx.checkToken()

        event.preventDefault();

        const data = new FormData(event.currentTarget);

        console.log({
            login: data.get("login"),
            password: data.get("password"),
            pesel: data.get("pesel"),
            role: value
        });

        addNewAccount({
            login: data.get("login"),
            password: data.get("password"),
            pesel: data.get("pesel"),
            role: value
        });
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
                        Dodaj Konto
                    </Typography>

                    <Box component="form" onSubmit={handleSubmit} noValidate sx={{mt: 1}}>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="pesel"
                            label="Pesel"
                            name="pesel"
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="login"
                            label="Login"
                            name="login"
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="password"
                            label="Hasło"
                            name="password"
                        />

                        <SelectAccountType value={value} setValue={setValue}/>

                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{mt: 3, mb: 2}}
                        >
                            Dodaj
                        </Button>

                    </Box>
                </Box>


            </Container>
        </Box>

    );
}