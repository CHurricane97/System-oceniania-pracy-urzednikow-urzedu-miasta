import NavBar from "../components/NavBar";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import CssBaseline from "@mui/material/CssBaseline";
import * as React from "react";
import {
    Pagination,
    Paper,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
} from "@mui/material";
import {useCallback, useContext, useEffect, useState} from "react";
import {appConfig} from "../config";
import Context from "../context/context";
import {useParams} from "react-router-dom";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";

export default function Unfinished_MotionsFor_Person() {

    let {pesel} = useParams();
    const [rows, setRows] = useState([])
    const ctx = useContext(Context)
    const [page, setPage] = useState(1);

    const handleChange = (event, value) => {
        ctx.checkToken()
        setPage(value);
    };


    const getSearch = useCallback(() => {
        const xHttp = new XMLHttpRequest();
        let json;
        let obj;
        xHttp.onreadystatechange = function () {

            if (this.readyState === 4 && this.status === 200) {
                json = xHttp.responseText;

                obj = JSON.parse(json);
                setRows(JSON.parse(JSON.stringify(obj)))

            }
            if (this.readyState === 4 && this.status === 400) {
                console.log("No access.");
            }
        };

        xHttp.open(
            "GET",
            `${appConfig.server}/motion/getAllNotFinishedMotionsByPesel?pesel=${pesel}&page=${page}`,
            true,
            null,
            null
        );
        xHttp.setRequestHeader('Authorization', 'Bearer ' + ctx.authToken)
        xHttp.send();

    }, [ctx.authToken, page, pesel]);

    useEffect(() => {
        getSearch();
    }, [getSearch]);

    const approve = async (data) => {
        const url = `${appConfig.server}/action/Approve`;
        try {
            const response = await fetch(`${url}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + ctx.authToken
                },
                body: JSON.stringify({
                    motionID: data.motionID
                }),
            });
            const resp = await response.json();

            alert(resp.message);


        } catch (e) {
            alert("Connection lost");
        }
    };

    const consult = async (data) => {
        const url = `${appConfig.server}/action/Consult`;
        try {
            const response = await fetch(`${url}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + ctx.authToken
                },
                body: JSON.stringify({
                    motionID: data.motionID
                }),
            });
            const resp = await response.json();

            alert(resp.message);


        } catch (e) {
            alert("Connection lost");
        }
    };

    const proceed = async (data) => {
        const url = `${appConfig.server}/action/Proced`
        try {
            const response = await fetch(`${url}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + ctx.authToken
                },
                body: JSON.stringify({
                    motionID: data.motionID
                }),
            });
            const resp = await response.json();

            alert(resp.message);


        } catch (e) {
            alert("Connection lost");
        }
    };

    const deny = async (data) => {
        const url = `${appConfig.server}/action/Deny`;
        try {
            const response = await fetch(`${url}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + ctx.authToken
                },
                body: JSON.stringify({
                    motionID: data.motionID
                }),
            });
            const resp = await response.json();

            alert(resp.message);


        } catch (e) {
            alert("Connection lost");
        }
    };


    return (
        <Box>
            <NavBar/>
            <Container className="blackout">
                <Typography sx={{
                    textAlign: "center",
                    mt: 4
                }} component="h1" variant="h4">
                    Zarządzaj nierozpatrzonymi wnioskami:
                </Typography>
                <Stack spacing={2}>
                    <Typography>Strona: {page}</Typography>
                    <Pagination count={Math.ceil(rows.count / 20)} page={page} onChange={handleChange}/>
                </Stack>

                <TableContainer component={Paper}>
                    <Table sx={{minWidth: 650}} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell align="center">ID Wniosku</TableCell>
                                <TableCell align="center">Stan Wniosku</TableCell>
                                <TableCell align="center">Typ Wniosku</TableCell>

                                <TableCell align="center"/>
                                <TableCell align="center"/>
                                <TableCell align="center"/>
                                <TableCell align="center"/>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.motionDetailsList?.map((row) => (
                                <TableRow
                                    key={row.motionId}
                                    sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                                    <TableCell align="center">{row.motionId}</TableCell>
                                    <TableCell align="center">{row.motionStateByMotionStateState}</TableCell>
                                    <TableCell align="center">{row.motionTypeByMotionTypeType}</TableCell>


                                    <TableCell align="left">
                                        <Button variant="contained" onClick={() => {
                                            ctx.checkToken()
                                            proceed({motionID: row.motionId}).then(()=>{
                                                row.motionStateByMotionStateState = "Przetwarzany"
                                                setRows(JSON.parse(JSON.stringify(rows)))
                                            })

                                        }}>
                                            Proceduj Wniosek
                                        </Button>
                                    </TableCell>
                                    <TableCell align="left">
                                        <Button variant="contained" onClick={() => {
                                            ctx.checkToken()
                                            consult({motionID: row.motionId}).then(()=>{
                                                row.motionStateByMotionStateState = "Przetwarzany"
                                                setRows(JSON.parse(JSON.stringify(rows)))
                                            })
                                        }}>
                                            Skonsultuj Wnioskodawcę
                                        </Button>
                                    </TableCell>


                                    <TableCell align="left">
                                        <Button variant="contained" onClick={() => {
                                            ctx.checkToken()
                                            approve({motionID: row.motionId}).then(()=>{
                                                rows.motionDetailsList = rows.motionDetailsList.filter((id)=>row.motionId !== id.motionId)
                                                console.log(rows.motionDetailsList)
                                                setRows(JSON.parse(JSON.stringify(rows)))
                                            })
                                        }}>
                                            Zatwierdź Wniosek
                                        </Button>
                                    </TableCell>
                                    <TableCell align="left">
                                        <Button variant="contained" onClick={() => {
                                            ctx.checkToken()
                                            deny({motionID: row.motionId}).then(()=>{
                                                rows.motionDetailsList = rows.motionDetailsList.filter((id)=>row.motionId !== id.motionId)
                                                console.log(rows.motionDetailsList)
                                                setRows(JSON.parse(JSON.stringify(rows)))
                                            })
                                        }}>
                                            Odrzuć wniosek
                                        </Button>
                                    </TableCell>

                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>

            </Container>
        </Box>

    )


}