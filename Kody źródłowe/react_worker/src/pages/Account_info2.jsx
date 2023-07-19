import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import * as React from "react";
import NavBar from "../components/NavBar";
import {useParams} from "react-router-dom";
import {useCallback, useContext, useEffect, useState} from "react";
import {appConfig} from "../config";
import Context from "../context/context";
import EditAccountDialog from "../dialogs/EditAccountDialog";


export default function Account_info2() {

    let {pesel} = useParams();
    const [person, setPerson] = useState({})
    const [userChange, setUserChange] = useState({})
    const [open, setOpen] = React.useState(false);
    const ctx = useContext(Context)

    const getPerson = useCallback(() => {
        const xHttp = new XMLHttpRequest();
        let json;
        let obj;
        xHttp.onreadystatechange = function () {

            if (this.readyState === 4 && this.status === 200) {
                json = xHttp.responseText;

                obj = JSON.parse(json);
                setPerson(JSON.parse(JSON.stringify(obj)));
                setUserChange(JSON.parse(JSON.stringify(obj)))

            }
            if (this.readyState === 4 && this.status === 400) {
                console.log("No access.");
            }
        };

        xHttp.open(
            "GET",
            `${appConfig.server}/person/getdatapesel?pesel=${pesel}`,
            true,
            null,
            null
        );
        xHttp.setRequestHeader('Authorization', 'Bearer ' + ctx.authToken)
        xHttp.send();

    }, [ctx.authToken, pesel]);

    useEffect(() => {
        getPerson();
    }, [getPerson]);


    const handleClickOpen = () => {
        ctx.checkToken()
        setOpen(true);
    };




    return (
        <Box>

            <NavBar/>
            <Container className="blackout" component="main" maxWidth="xs">
                <EditAccountDialog
                    open={open}
                    setOpen={setOpen}
                    setUserChange={setUserChange}
                    user={person}
                    userChange={userChange}/>

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
                    <Box sx={{mt: 1}}>

                        <Typography className="left" component="h3" id={"name"}>
                            Imię: {userChange.name}
                        </Typography>
                        <Typography className="left" component="h3" id={"surname"}>
                            Nazwisko: {userChange.surname}
                        </Typography>
                        <Typography className="left" component="h3" id={"pesel"}>
                            Pesel: {userChange.pesel}
                        </Typography>
                        <Typography className="left" component="h3" id={"dob"}>
                            Data Urodzenia: {userChange.dateOfBirth}
                        </Typography>
                        <Typography className="left" component="h3" id={"city"}>
                            Miasto: {userChange.city}
                        </Typography>
                        <Typography className="left" component="h3" id={"postcode"}>
                            Kod Pocztowy: {userChange.cityCode}
                        </Typography>
                        <Typography className="left" component="h3" id={"street"}>
                            Ulica: {userChange.street}
                        </Typography>
                        <Typography className="left" component="h3" id={"house_number"}>
                            Numer Domu: {userChange.houseNumber}
                        </Typography>
                        <Typography className="left" component="h3" id={"flat_number"}>
                            Numer Mieszkania: {userChange.flatNumber}
                        </Typography>


                        <Button
                            fullWidth
                            variant="contained"
                            onClick={handleClickOpen}
                            sx={{mt: 3, mb: 2}}
                        >
                            Edytuj Dane
                        </Button>

                    </Box>
                </Box>


            </Container>
        </Box>

    );
}