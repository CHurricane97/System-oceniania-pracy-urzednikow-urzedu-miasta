import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import * as React from "react";
import NavBar from "../components/NavBar";
import {useNavigate} from "react-router-dom";
import {useContext} from "react";
import Context from "../context/context";

export default function Menu_Worker() {

    const navigate = useNavigate()
    const ctx = useContext(Context)

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

                    }}>

                    <Typography className="test" component="h1" variant="h4">
                        Menu Główne:
                    </Typography>

                    <Box component="form"  noValidate sx={{ mt: 1 }}>

                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            onClick={()=>{
                                ctx.checkToken()
                                navigate("/accountInfo")
                            }}>
                            Szczegóły konta
                        </Button>

                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            onClick={()=>{
                                ctx.checkToken()
                                navigate("/addMotion")
                            }}
                        >
                            Dodawanie nowego wniosku
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            onClick={()=>{
                                ctx.checkToken()
                                navigate("/findPersonForMotion")
                            }}
                        >
                            Zarządzaj nierozpatrzonymi wnioskami
                        </Button>




                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            onClick={()=>{
                                ctx.checkToken()
                                navigate("/addNewPerson")
                            }}

                        >
                            Dodawanie nowych Danych osobowych
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            onClick={()=>{
                                ctx.checkToken()
                                navigate("/findPerson")
                            }}
                        >
                            Wyszukaj Osobę
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            onClick={()=>{
                                ctx.checkToken()
                                navigate("/addNewAccount")
                            }}
                        >
                            Dodawanie Nowych kont
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            onClick={()=>{
                                ctx.checkToken()
                                navigate("/changePasswordOther")
                            }}
                        >
                            Administracyjna Zmiana Hasła
                        </Button>

                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            onClick={()=>{
                                ctx.checkToken()
                                navigate("/addMotionType")
                            }}
                        >
                            Dodanie nowych typów wniosków
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            onClick={()=>{
                                ctx.checkToken()
                                navigate("/loginRegister")
                            }}

                        >
                            Wyświetl Rejestr Logowań
                        </Button>

                    </Box>
                </Box>
            </Container>
        </Box>

    );
}