import * as React from 'react';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import NavBar from "../components/NavBar";
import {FormControl, InputLabel, Select} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import {useCallback, useContext, useEffect, useState} from "react";
import {appConfig} from "../config";
import Context from "../context/context";
import {useNavigate} from "react-router-dom";


const theme = createTheme();

export default function Add_New_Motion() {

    const [motionTypes, setMotionTypes] = useState([])
    const [motionType, setMotionType] = useState('')
    const ctx = useContext(Context)
    const navigate = useNavigate()

    const addMotion = async (data) => {
        const url = `${appConfig.server}/motion/add`;
        try {
            const response = await fetch(`${url}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + ctx.authToken
                },
                body: JSON.stringify({
                    motiontype: data.motiontype,
                    pesel: data.pesel
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

    const getTypes = useCallback(() => {
        const xHttp = new XMLHttpRequest();
        let json;
        let obj;
        xHttp.onreadystatechange = function () {

            if (this.readyState === 4 && this.status === 200) {
                json = xHttp.responseText;

                obj = JSON.parse(json);
                setMotionTypes(obj)
            }
            if (this.readyState === 4 && this.status === 400) {
                console.log("No access.");
            }
        };

        xHttp.open(
            "GET",
            `${appConfig.server}/utility/getAllMotionTypes`,
            true,
            null,
            null
        );
        xHttp.send();

    }, []);

    useEffect(() => {
        getTypes();
    }, [getTypes]);

    const handleSubmit = (event) => {
        ctx.checkToken()
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        addMotion({
            motiontype: motionType,
            pesel: data.get("pesel")
        });
    };

    function handleChangeType(event) {
        ctx.checkToken()
        setMotionType(event.target.value)
    }

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
                        Dodaj Wniosek
                    </Typography>
                    <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="pesel"
                            label="Pesel"
                            name="pesel"
                            autoComplete="pesel"
                            autoFocus
                        />

                        <Box >
                            <FormControl fullWidth>
                                <InputLabel id="label">
                                    Typ Wniosku
                                </InputLabel>
                                <Select

                                    labelId="label"
                                    label="Typ Wniosku"
                                    id="status"
                                    value={motionType}
                                    onChange={handleChangeType}>

                                    {motionTypes.map((option) => (
                                        <MenuItem key={option.motionTypeId} value={option.motionTypeId}>
                                            {option.type}
                                        </MenuItem>
                                    ))}

                                </Select>
                            </FormControl>
                        </Box>

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