import {createTheme, ThemeProvider} from "@mui/material/styles";
import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import Grid from "@mui/material/Grid";
import * as React from "react";
import NavBar from "../components/NavBar";
import {useNavigate} from "react-router-dom";
import {useContext} from "react";
import Context from "../context/context";

const theme = createTheme();

export default function Menu_Citizen() {

    const navigate = useNavigate()
    const ctx = useContext(Context)

    function handleAccountInfo(){
        ctx.checkToken()
        navigate("/accountInfo")
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

                    <Typography className={"top"} component="h1" variant="h4">
                        Menu Główne:
                    </Typography>
                    <Box component="form" noValidate sx={{mt: 1}}>

                        <Button
                            fullWidth
                            variant="contained"
                            sx={{mt: 3, mb: 2}}
                            onClick={handleAccountInfo}
                        >
                            Szczegóły konta
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{mt: 3, mb: 2}}
                            onClick={()=>{
                                ctx.checkToken()
                                navigate("/myMotions")
                            }}
                        >
                            Sprawdź status wniosków
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{mt: 3, mb: 2}}
                            onClick={()=>{
                                ctx.checkToken()
                                navigate("/searchWorkers")
                            }}

                        >
                            Przeglądaj Pracowników
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{mt: 3, mb: 2}}
                            onClick={()=>{
                                ctx.checkToken()
                                navigate("/marks")
                            }}
                        >
                            Oceń Pracowników
                        </Button>

                    </Box>
                </Box>


            </Container>
        </Box>

    );
}