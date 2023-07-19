import {TableCell, TableRow} from "@mui/material";
import Button from "@mui/material/Button";
import * as React from "react";
import {useCallback, useContext, useEffect, useState} from "react";
import {appConfig} from "../config";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Context from "../context/context";
import AddEvaluationDialog from "../dialog/AddEvaluationDialog";


export default function MotionTableRow({row, motionId}) {

    const [state, setState] = useState(false)
    const [openReview, setOpenReview] = useState(false);
    const [value, setValue] = React.useState(2);
    const [description, setDescription] = useState('')
    const ctx = useContext(Context)

    const getState = useCallback(() => {
        const xHttp = new XMLHttpRequest();
        let json;
        let obj;
        xHttp.onreadystatechange = function () {

            if (this.readyState === 4 && this.status === 200) {
                json = xHttp.responseText;

                obj = JSON.parse(json);
                setState(obj)

            }
            if (this.readyState === 4 && this.status === 400) {
                console.log("No access.");
            }
        };

        xHttp.open(
            "GET",
            `${appConfig.server}/evaluation/checkIfWorkerEvaluatedForMotion?motionID=${motionId}&personID=${row.personalDataId
            }`,
            true,
            null,
            null
        );
        xHttp.setRequestHeader('Authorization', 'Bearer ' + ctx.authToken)
        xHttp.send();

    }, [ctx.authToken, motionId, row.personalDataId]);

    useEffect(() => {
        getState();
    }, [getState])

    const deleteReview = async (data) => {
        try {
            const response = await fetch(`${appConfig.server}/evaluation/deleteEvaluation`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + ctx.authToken
                },
                body: JSON.stringify({
                    motionId: data.motionId,
                    workerId: data.workerId
                }),
            });
            const resp = await response.json();

            alert(resp.message)

        } catch (e) {
            alert("Connection lost");
        }
    };

    return (
        <TableRow
            key={row.personalDataId}
            sx={{'&:last-child td, &:last-child th': {border: 0}}}>
            <TableCell align="center">{row.personalDataId}</TableCell>
            <TableCell align="center">{row.name + ' ' + row.surname}</TableCell>
            <TableCell align="center"/>
            <TableCell align="left">
                {row.actionTakenInMotionsByPersonalDataId.map((motion) => (
                    <Box>
                        {motion.type}
                    </Box>
                ))}
            </TableCell>


            <TableCell align="left">

                <AddEvaluationDialog motionId={motionId}
                                     value={value}
                                     setValue={setValue}
                                     setDescription={setDescription}
                                     description={description}
                                     workerId={row.personalDataId}
                                     openReview={openReview}
                                     setOpenReview={setOpenReview} state={state} setState={setState}/>

                <Button variant="contained" onClick={() => {

                    ctx.checkToken()
                    if(state === false) {
                        setOpenReview(true)
                        setValue(8)
                        setDescription('')
                    }else{
                        deleteReview({motionId: motionId, workerId: row.personalDataId}).then(()=>{setState(!state)})
                    }
                }}>
                    {state !== true ? 'Oceń Pracownika' : 'Usuń Ocenę'}
                </Button>
            </TableCell>

        </TableRow>
    );
}