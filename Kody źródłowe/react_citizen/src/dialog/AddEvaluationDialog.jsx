import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Rating,
    TextField
} from "@mui/material";
import * as React from "react";
import {useContext} from "react";
import {Stack} from "@mui/system";
import Typography from "@mui/material/Typography";
import {appConfig} from "../config";
import Context from "../context/context";

const StackStyle = {
    marginBottom: 2
}

export default function AddEvaluationDialog({
                                                motionId,
                                                workerId,
                                                openReview,
                                                value,
                                                description,
                                                setValue,
                                                setOpenReview,
                                                setDescription,
                                                setState,
                                                state
                                            }) {
    const ctx = useContext(Context)

    const addReview = async (data) => {
        try {
            const response = await fetch(`${appConfig.server}/evaluation/addEvaluation`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + ctx.authToken
                },
                body: JSON.stringify({
                    description: data.description,
                    grade: data.grade,
                    motionId: data.motionId,
                    workerId: data.workerId
                }),
            });
            const resp = await response.json();

            if (response.ok) {
                alert(resp.message)
                setState(!state)
                setOpenReview(false);
            } else {
                alert(resp.message)
                setOpenReview(false);
            }

        } catch (e) {
            alert("Connection lost");
        }
    };

    const handleCloseReview = () => {
        ctx.checkToken()
        setOpenReview(false);
    };

    function handleConfirmReview() {
        ctx.checkToken()
        addReview({motionId: motionId, grade: value, description: description, workerId: workerId})
            .then(() => {
            })
    }

    function handleDescriptionChange(event) {
        setDescription(event.target.value)
    }

    return (
        <Dialog open={openReview} onClose={handleCloseReview}>
            <DialogTitle>Ocena</DialogTitle>
            <DialogContent>
                <Stack spacing={2} sx={StackStyle}>
                    <Rating
                        name="simple-controlled"
                        value={value}
                        max={10}
                        onChange={(event, newValue) => {
                            setValue(newValue);
                        }}/>

                    <TextField
                        multiline
                        minRows={2}
                        id="standard-basic"
                        variant="outlined"
                        onChange={handleDescriptionChange}
                        placeholder="Komentarz"/>

                    <Typography>
                        Maksymalna długość: {description.length}/1023
                    </Typography>
                </Stack>

            </DialogContent>
            <DialogActions>
                <Button onClick={handleCloseReview}>Anuluj</Button>
                <Button onClick={handleConfirmReview}>Dodaj</Button>
            </DialogActions>
        </Dialog>
    );
}