import NavBar from "../components/NavBar";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import CssBaseline from "@mui/material/CssBaseline";
import * as React from "react";
import {
    FormControl,
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
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import SearchIcon from "@mui/icons-material/Search";
import {styled} from "@mui/system";
import Grid from "@mui/material/Grid";
import {useNavigate} from "react-router-dom";

const StyledSearchButton = styled(Button)(() => ({

}));

const StyledIcon = {
    margin: 1
}

const BoxStyle = {
    display: "inline-block",
    margin: 1
}

const TextFieldStyle = {
    margin: 1,
    backgroundColor: "#e8eaf6",
}

const FormControlStyle = {
    m: 2,
}

export default function WorkerList() {

    const [rows, setRows] = useState([])
    const ctx = useContext(Context)
    const [page, setPage] = useState(1);
    const [name, setName] = useState('')
    const [surname, setSurname] = useState('')
    let [searchName] = useState('')
    let [searchSurname] = useState('')
    const navigate = useNavigate()


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
            `${appConfig.server}/person/getWorkersFiltered?page=${page}&surname=${surname}&name=${name}`,
            true,
            null,
            null
        );
        xHttp.setRequestHeader('Authorization', 'Bearer ' + ctx.authToken)
        xHttp.send();

    }, [ctx.authToken, name, page, surname]);

    useEffect(() => {
        getSearch();
    }, [getSearch])

    function handleSearchClients(event) {

        ctx.checkToken()

        event.preventDefault(true);

        setName(searchName)
        setSurname(searchSurname)

    }

    function handleChangeSurname(event) {
        searchSurname = event.target.value
    }

    function handleChangeName(event) {
        searchName = event.target.value
    }


    return (
        <Box>
            <NavBar/>


            <Container className="blackout">
                <Grid container  sx={{justifyContent: "center"}}>
                    <form onSubmit={handleSearchClients}>
                        <Typography sx={{
                            textAlign: "center",
                            mt: 4
                        }} component="h1" variant="h4">
                            Wyszukaj pracownika:
                        </Typography>

                        <Box sx={BoxStyle}>
                            <TextField
                                margin="normal"
                                sx={TextFieldStyle}
                                fullWidth
                                variant="outlined"
                                id="name"
                                label="Imię"
                                name="name"
                                autoComplete="name"
                                onChange={handleChangeName}/>
                        </Box>

                        <Box sx={BoxStyle}>
                            <TextField
                                margin="normal"
                                sx={TextFieldStyle}
                                fullWidth
                                variant="outlined"
                                id="surname"
                                label="Nazwisko"
                                name="surname"
                                autoComplete="surname"
                                onChange={handleChangeSurname}/>
                        </Box>

                        <FormControl variant="standard" sx={FormControlStyle}>
                            <StyledSearchButton variant="contained" type="submit">
                                <Typography sx={StyledIcon}>
                                    Filtruj
                                </Typography>
                            </StyledSearchButton>
                        </FormControl>

                    </form>
                </Grid>


                <Stack spacing={2} sx={{marginTop: 4}}>
                    <Typography>Strona: {page}</Typography>
                    <Pagination count={Math.ceil(rows.count / 20)} page={page} onChange={handleChange}/>
                </Stack>

                <TableContainer component={Paper}>
                    <Table sx={{minWidth: 650}} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell align="center">ID Pracownika</TableCell>
                                <TableCell align="center">Imię i nazwisko pracownika</TableCell>
                                <TableCell align="center"/>
                                <TableCell align="center"/>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.workerList?.map((row) => (
                                <TableRow
                                    key={row.personalDataId}
                                    sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                                    <TableCell align="center">{row.personalDataId}</TableCell>
                                    <TableCell align="center">{row.name} {row.surname}</TableCell>
                                    <TableCell align="center"/>


                                    <TableCell align="center">
                                        <Button variant="contained" onClick={() => {
                                            ctx.checkToken()
                                            navigate(`/workerMarks/${row.personalDataId}`)
                                        }}>
                                            Szczegóły
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