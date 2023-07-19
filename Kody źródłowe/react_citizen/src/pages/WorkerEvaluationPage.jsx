import NavBar from "../components/NavBar";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import CssBaseline from "@mui/material/CssBaseline";
import * as React from "react";
import {
    FormControl,
    Pagination,
    Paper, Rating,
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
import {useParams} from "react-router-dom";

const StyledPaper = {
    p: 3,
    flexGrow: 1,
    overflow: "auto",
    marginBottom: 5
}

const GridItem = {
    marginBottom: 3
}

const GridStyle = {
    flexDirection: "row-reverse"
}

export default function WorkerEvaluationPage() {

    let {personalDataId} = useParams()
    const ctx = useContext(Context)
    const [page, setPage] = useState(1);
    const [reviews, setReviews] = useState([])
    const [avg, setAvg] = useState(0)


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
                setReviews(JSON.parse(JSON.stringify(obj)))

            }
            if (this.readyState === 4 && this.status === 400) {
                console.log("No access.");
            }
        };

        xHttp.open(
            "GET",
            `${appConfig.server}/evaluation/getEvaluationListForWorkerWithAvg?page=${page}&personaldataid=${personalDataId}`,
            true,
            null,
            null
        );
        xHttp.setRequestHeader('Authorization', 'Bearer ' + ctx.authToken)
        xHttp.send();

    }, [ctx.authToken, page, personalDataId]);

    useEffect(() => {
        getSearch();
    }, [getSearch])

    useEffect(() => {
        if(reviews.averageRounded !== undefined) {
            setAvg(JSON.parse(JSON.stringify(reviews.averageRounded)));
        }
    }, [reviews, reviews.averageRounded])


    return (
        <Box>
            <NavBar/>


            <Container className="blackout">


                <Grid container  sx={{justifyContent: "center"}}>
                    <Typography sx={{
                        textAlign: "center",
                        mt: 4,
                        mb: 4
                    }} component="h1" variant="h4">
                        Oceny Dla Pracownika: {reviews.personalDataForEvaluationName} {reviews.personalDataForEvaluationSurname}
                    </Typography>
                </Grid>

                <Grid container>
                    <Paper sx={{width: "100%"}}>
                        <Grid item container sx={{minHeight:100, justifyContent:"center", alignItems:"center", flexDirection:"column"}}>
                            <Typography
                                gutterBottom
                                variant="h7"
                                component="a">
                               Średnia Ocen: {reviews.average?.toFixed(2)}
                            </Typography>
                            <Grid item sx={{GridItem, textAlign:"center"}}>
                                <Rating name="read-only" precision={0.5} size="large" value={avg} max={10} readOnly/>
                            </Grid>
                        </Grid>
                    </Paper>
                </Grid>

                <Stack spacing={2}>
                    <Typography>Strona: {page}</Typography>
                    <Pagination count={Math.ceil(reviews.count / 20)} page={page} onChange={handleChange}/>
                </Stack>

                {reviews.evaluationList?.map((row) => (

                    <Grid container>
                        <Paper
                            sx={StyledPaper}>


                            <Grid item sx={GridItem}>
                                <Typography
                                    gutterBottom
                                    variant="h5"
                                    component="a">
                                   Oceniający: {row.motionForEvaluationPersonalDataName}
                                </Typography>
                            </Grid>

                            <Typography sx={{textAlign:"center"}} variant="body1" gutterBottom>
                                Ocena:
                            </Typography>
                            <Grid item sx={{GridItem, textAlign:"center", mb:4}}>


                                <Rating name="read-only" value={row.grade} max={10} readOnly/>
                            </Grid>


                            <Grid item sx={GridItem}>
                                <Typography variant="body1" gutterBottom>
                                    Komentarz: {row.description}
                                </Typography>
                            </Grid>

                            <Grid item container sx={GridStyle}>
                                <Typography
                                    gutterBottom
                                    variant="h7"
                                    component="a">
                                    {row.motionForEvaluationMotionType}
                                </Typography>
                            </Grid>

                        </Paper>
                    </Grid>
                ))}

            </Container>
        </Box>

    )


}