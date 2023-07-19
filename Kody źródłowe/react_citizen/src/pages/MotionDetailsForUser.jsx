import NavBar from "../components/NavBar";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";

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
import {useNavigate, useParams} from "react-router-dom";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import MotionTableRow from "../components/MotionTableRow";
import {color} from "@mui/system";

export default function MotionDetailsForUser() {

    let {motionId} = useParams()
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
            `${appConfig.server}/person/getAllWorkersForMotion?page=${page}&motionId=${motionId}`,
            true,
            null,
            null
        );
        xHttp.setRequestHeader('Authorization', 'Bearer ' + ctx.authToken)
        xHttp.send();

    }, [ctx.authToken, motionId, page]);

    useEffect(() => {
        getSearch();
    }, [getSearch])


    return (
        <Box>
            <NavBar/>


            <Container className="blackout">
                <Typography sx={{
                    textAlign: "center",
                    mt: 4
                }} component="h1" variant="h4">
                    Wybierz pracownika do oceny:
                </Typography>

                <Stack spacing={2}>
                    <Typography>Strona: {page}</Typography>
                    <Pagination count={Math.ceil(rows.count / 20)} page={page} onChange={handleChange}/>
                </Stack>

                <TableContainer component={Paper}  >
                    <Table sx={{minWidth: 650}} aria-label="simple table">
                        <TableHead>
                            <TableRow >
                                <TableCell align="center">ID Pracownika</TableCell>
                                <TableCell align="center">Imię i nazwisko</TableCell>
                                <TableCell align="center"/>
                                <TableCell align="left">Akcje wykonane w związku z wnioskiem</TableCell>
                                <TableCell align="center"/>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows?.map((row) => (
                                <MotionTableRow row={row} motionId={motionId}/>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>

            </Container>
        </Box>

    )


}