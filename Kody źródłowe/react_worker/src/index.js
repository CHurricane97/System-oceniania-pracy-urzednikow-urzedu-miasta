import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {BrowserRouter} from "react-router-dom";
import { ContextProvider } from "./context/context";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import {Paper} from "@mui/material";

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    <body>
    <React.StrictMode>
        <BrowserRouter>
            <ContextProvider>
                <App />
            </ContextProvider>
        </BrowserRouter>
    </React.StrictMode>
    <footer contentEditable></footer>
    <Paper sx={{
        width: '100%',
        bottom: 0,
    }} component="footer" square variant="outlined">
        <Container maxWidth="lg">
            <Box
                sx={{
                    flexGrow: 1,
                    justifyContent: "center",
                    display: "flex",
                    my:1
                }}
            >

            </Box>

            <Box
                sx={{
                    flexGrow: 1,
                    justifyContent: "center",
                    display: "flex",
                    mb: 2,
                }}
            >
                <Typography variant="caption" color="initial">
                    Copyright Bartosz Sulima ©2023.
                </Typography>
            </Box>
        </Container>
    </Paper>
    </body>

);


reportWebVitals();
