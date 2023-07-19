import * as React from 'react';

import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';

import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';

import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import NavBar from "../components/NavBar";
import {appConfig} from "../config";
import {useContext} from "react";
import Context from "../context/context";
import {useNavigate} from "react-router-dom";

const theme = createTheme();

export default function Add_New_Person() {

    const ctx = useContext(Context)
    const navigate = useNavigate()

    const addNewPerson = async (data) => {
        const url = `${appConfig.server}/person/add`;
        try {
            const response = await fetch(`${url}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + ctx.authToken
                },
                body: JSON.stringify({
                    city: data.city,
                    city_code: data.city_code,
                    date_of_birth: data.date_of_birth,
                    flat_number: data.flat_number,
                    house_number: data.house_number,
                    name: data.name,
                    pesel: data.pesel,
                    street: data.street,
                    surname: data.surname
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



        addNewPerson({
            city: data.get("city"),
            city_code: data.get("city_code"),
            date_of_birth: data.get("date"),
            flat_number: data.get("flat_number"),
            house_number: data.get("house_number"),
            name: data.get("name"),
            pesel: data.get("pesel"),
            street: data.get("street"),
            surname: data.get("surname")
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
                        Dodaj nową osobę do systemu:
                    </Typography>

                    <Box component="form" onSubmit={handleSubmit} noValidate sx={{mt: 1}}>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="name"
                            label="Imię"
                            name="name"
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="surname"
                            label="Nazwisko"
                            type="surname"
                            id="surname"
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="pesel"
                            label="Pesel"
                            type="pesel"
                            id="pesel"
                        />


                        <TextField
                            margin="normal"
                            fullWidth
                            InputLabelProps={{ shrink: true }}
                            type="date"
                            variant="outlined"
                            id="date"
                            label="Data Urodzenia"
                            name="date"/>

                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="city"
                            id="city"
                            label="Miasto"
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="city_code"
                            id="city_code"
                            label="Kod Pocztowy"

                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="street"
                            id="street"
                            label="Ulica"

                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="house_number"
                            id="house_number"
                            label="Numer Domu"

                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="flat_number"
                            id="flat_number"
                            label="Numer Mieszkania"

                        />

                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{mt: 3, mb: 2}}
                        >
                            Dodaj Osobę
                        </Button>

                    </Box>
                </Box>


            </Container>
        </Box>

    );
}