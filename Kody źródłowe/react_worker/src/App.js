import SignIn from "./pages/SignIn";
import {Navigate, Route, Routes} from "react-router-dom";
import Account_info from "./pages/Account_info";
import Menu_Worker from "./pages/Menu_Worker";
import Change_Password_User from "./pages/Change_Password_User";
import {useContext} from "react";
import Context from "./context/context";
import {createTheme, ThemeProvider} from "@mui/material/styles";
import Add_New_Person from "./pages/Add_New_Person";
import Add_New_Account from "./pages/Add_New_Account";
import Find_Person from "./pages/Find_Person";
import Account_info2 from "./pages/Account_info2";
import Change_Password_Other_User from "./pages/Change_Password_Other_User";
import Add_New_MotionType from "./pages/Add_New_MotionType";
import Add_New_Motion from "./pages/Add_New_Motion";
import Find_PersonforMotion from "./pages/Find_PersonforMotion";
import Unfinished_MotionsFor_Person from "./pages/Unfinished_Motions For_Person";
import Login_Register from "./pages/Login_Register";
import {BottomNavigation, BottomNavigationAction, GlobalStyles, Paper} from "@mui/material";
import CssBaseline from "@mui/material/CssBaseline";
import * as React from "react";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";

const theme = createTheme();

theme.palette.primary.main = "#283593";

function App() {

    const ctx = useContext(Context)

    return (
        <Box>
        <Box sx={{flexGrow: 1, minHeight: "100%"}}>
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <GlobalStyles
                cla
                styles={{
                    body: { backgroundColor: "#e0e0e0" },
                }}
            />
            <Routes>

                <Route path="/" element={<SignIn/>}/>

                <Route path="/changePassword" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Change_Password_User/>
                    )}/>

                <Route path="/menuWorker" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Menu_Worker/>
                    )}/>

                <Route path="/accountInfo" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Account_info/>
                    )}/>

                <Route path="/addNewPerson" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Add_New_Person/>
                    )}/>

                <Route path="/addNewAccount" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Add_New_Account/>
                    )}/>

                <Route path="/findPerson" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Find_Person/>
                    )}/>

                <Route path="/person/:pesel" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Account_info2/>
                    )}/>

                <Route path="/changePasswordOther" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Change_Password_Other_User/>
                    )}/>

                <Route path="/addMotionType" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Add_New_MotionType/>
                    )}/>

                <Route path="/addMotion" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Add_New_Motion/>
                    )}/>

                <Route path="/findPersonForMotion" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Find_PersonforMotion/>
                    )}/>

                <Route path="/getAllNotFinishedMotionsByPesel/:pesel" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Unfinished_MotionsFor_Person/>
                    )}/>

                <Route path="/loginRegister" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Login_Register/>
                    )}/>

            </Routes>

        </ThemeProvider>
            </Box>
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
                    Copyright ©2022. [] Limited
                </Typography>
            </Box>
        </Container>
    </Paper>
    </Box>
    );
}

export default App;
