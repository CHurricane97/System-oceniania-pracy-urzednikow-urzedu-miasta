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
import {type} from "@testing-library/user-event/dist/type";

export default function Login_Register() {
    
    const [rows, setRows] = useState([])
    const ctx = useContext(Context)
    const [page, setPage] = useState(1);
    const [pageCount, setPageCount] = useState(1);

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
            `${appConfig.server}/person/getLoginRegister?page=${page}`,
            true,
            null,
            null
        );
        xHttp.setRequestHeader('Authorization', 'Bearer ' + ctx.authToken)
        xHttp.send();

    }, [ctx.authToken, page]);

    useEffect(() => {
        getSearch();
    }, [getSearch]);


    const getSearchNumber = useCallback(() => {
        const xHttp = new XMLHttpRequest();
        let json;
        let obj;
        xHttp.onreadystatechange = function () {

            if (this.readyState === 4 && this.status === 200) {
                json = xHttp.responseText;

                obj = JSON.parse(json);
                setPageCount(JSON.parse(JSON.stringify(obj)))

            }
            if (this.readyState === 4 && this.status === 400) {
                console.log("No access.");
            }
        };

        xHttp.open(
            "GET",
            `${appConfig.server}/person/getLoginRegisterCounter`,
            true,
            null,
            null
        );
        xHttp.setRequestHeader('Authorization', 'Bearer ' + ctx.authToken)
        xHttp.send();

    }, [ctx.authToken]);

    useEffect(() => {
        getSearchNumber();
    }, [getSearchNumber]);

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
                    <Pagination count={Math.ceil(pageCount / 20)} page={page} onChange={handleChange}/>
                </Stack>





                <TableContainer component={Paper}>
                    <Table sx={{minWidth: 650}} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell align="center">Login</TableCell>
                                <TableCell align="center">Typ konta</TableCell>
                                <TableCell align="center">Imię i nazwisko</TableCell>
                                <TableCell align="center">Data zalogowania</TableCell>

                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows?.map((row) => (
                                <TableRow
                                    key={row.motionId}
                                    sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                                    <TableCell align="center">{row.usersByUserIdLogin}</TableCell>
                                    <TableCell align="center">{row.usersByUserIdPermissionLevel === 'ROLE_ADMIN' ? 'Pracownik' : 'Obywatel'}</TableCell>
                                    <TableCell align="center">{row.usersByUserIdPersonalDataName +' '+row.usersByUserIdPersonalDataSurname}</TableCell>
                                    <TableCell align="center">{row.dateOfLogging}</TableCell>

                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>

            </Container>
        </Box>

    )


}