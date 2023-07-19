import * as React from 'react';

import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';

import Box from '@mui/material/Box';

import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import NavBar from "../components/NavBar";
import {appConfig} from "../config";
import {useContext} from "react";
import Context from "../context/context";
import {useNavigate} from "react-router-dom";

const theme = createTheme();

export default function Add_New_MotionType() {

    const ctx = useContext(Context)
    const navigate = useNavigate()

    const addMotionType = async (data) => {
        const url = `${appConfig.server}/utility/addMotionType`;
        try {
            const response = await fetch(`${url}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + ctx.authToken
                },
                body: JSON.stringify({
                    type: data.motiontype,
                }),
            });
            const resp = await response.json();

            alert(resp.message);

            if (response.ok){
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
        addMotionType({
            motiontype: data.get('motiontype'),
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
                        Dodaj Nowy Typ Wniosku:
                    </Typography>
                    <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="motiontype"
                            label="Typ Wniosku"
                            name="motiontype"
                            autoComplete="motiontype"
                            autoFocus
                        />


                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                        >
                            Dodaj
                        </Button>

                    </Box>
                </Box>


            </Container>
        </Box>

    );
}