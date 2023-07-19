import SignIn from "./pages/SignIn";
import {Navigate, Route, Routes} from "react-router-dom";
import Account_info from "./pages/Account_info";
import Menu_Citizen from "./pages/Menu_Citizen";
import Change_Password_User from "./pages/Change_Password_User";
import {useContext} from "react";
import Context from "./context/context";
import {createTheme, ThemeProvider} from "@mui/material/styles";
import Motions_for_evaluation from "./pages/Motions_for_evaluation";
import MotionDetailsForUser from "./pages/MotionDetailsForUser";
import WorkerList from "./pages/WorkerList";
import WorkerEvaluationPage from "./pages/WorkerEvaluationPage";
import MotionsState from "./pages/MotionsState";
import CssBaseline from "@mui/material/CssBaseline";
import {GlobalStyles} from "@mui/system";

const theme = createTheme();

theme.palette.primary.main = "#283593";

function App() {

    const ctx = useContext(Context)

    return (
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

                <Route path="/menuCitizen" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Menu_Citizen/>
                    )}/>

                <Route path="/accountInfo" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Account_info/>
                    )}/>

                <Route path="/mark/:motionId" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <MotionDetailsForUser/>
                    )}/>

                <Route path="/marks" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <Motions_for_evaluation/>
                    )}/>

                <Route path="/searchWorkers" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <WorkerList/>
                    )}/>

                <Route path="/workerMarks/:personalDataId" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <WorkerEvaluationPage/>
                    )}/>
                <Route path="/myMotions" element={
                    !ctx.isLoggedIn ? (
                        <Navigate to="/" replace />
                    ) : (
                        <MotionsState/>
                    )}/>


            </Routes>
        </ThemeProvider>
    );
}

export default App;
