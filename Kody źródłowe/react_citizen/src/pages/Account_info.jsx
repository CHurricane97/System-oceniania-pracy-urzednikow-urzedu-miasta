import {createTheme, ThemeProvider} from "@mui/material/styles";
import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import * as React from "react";
import NavBar from "../components/NavBar";
import {useCallback, useContext, useEffect, useState} from "react";
import {appConfig} from "../config";
import Context from "../context/context";
import {useNavigate} from "react-router-dom";

const theme = createTheme();

export default function Account_info() {

    const [accountData, setAccountData] = useState({})
    const ctx = useContext(Context)
    const navigate = useNavigate()

    function handleChangePassword() {
        ctx.checkToken()
        navigate("/changePassword")
    }

    const getAccountInf = useCallback(() => {
        const xHttp = new XMLHttpRequest();
        let json;
        let object;
        xHttp.onreadystatechange = function () {

            if (this.readyState === 4 && this.status === 200) {
                json = xHttp.responseText;

                object = JSON.parse(json);
                setAccountData(object)
            }
        };

        xHttp.open(
            "GET",
            `${appConfig.server}/person/getdatatoken`,
            true,
            null,
            null
        );
        xHttp.setRequestHeader('Authorization', 'Bearer ' + ctx.authToken)
        xHttp.send();

    }, [ctx.authToken]);


    useEffect(() => {
        getAccountInf();
    }, [getAccountInf]);


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

                    <Typography component="h1" variant="h4" className={"top"}>
                        Informacje o koncie:
                    </Typography>
                    <Box component="form" noValidate sx={{mt: 1}}>

                        <Typography className="left" component="h3" id={"name"}>
                            Imię: {accountData.name}
                        </Typography>
                        <Typography className="left" component="h3" id={"surname"}>
                            Nazwisko: {accountData.surname}
                        </Typography>
                        <Typography className="left" component="h3" id={"pesel"}>
                            Pesel: {accountData.pesel}
                        </Typography>
                        <Typography className="left" component="h3" id={"dob"}>
                            Data Urodzenia: {accountData.dateOfBirth}
                        </Typography>
                        <Typography className="left" component="h3" id={"city"}>
                            Miasto: {accountData.city}
                        </Typography>
                        <Typography className="left" component="h3" id={"postcode"}>
                            Kod Pocztowy: {accountData.cityCode}
                        </Typography>
                        <Typography className="left" component="h3" id={"street"}>
                            Ulica: {accountData.street}
                        </Typography>
                        <Typography className="left" component="h3" id={"house_number"}>
                            Numer Domu: {accountData.houseNumber}
                        </Typography>
                        <Typography className="left" component="h3" id={"flat_number"}>
                            Numer Mieszkania: {accountData.flatNumber}
                        </Typography>

                        <Button

                            fullWidth
                            variant="contained"
                            sx={{mt: 3, mb: 2}}
                            onClick={handleChangePassword}
                        >
                            Zmień hasło
                        </Button>

                    </Box>
                </Box>

            </Container>
        </Box>

    );
}