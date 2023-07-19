import {useContext} from "react";
import Context from "../context/context";
import {appConfig} from "../config";

import {Dialog, DialogActions, DialogContent, DialogTitle} from "@mui/material";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";


export default function EditAccountDialog({user, userChange, setUserChange, setOpen, open}){
    const ctx = useContext(Context)

    const editPerson = async (data) => {
        const url = `${appConfig.server}/person/update`;
        try {
            const response = await fetch(`${url}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + ctx.authToken
                },
                body: JSON.stringify({
                    city: data.city,
                    city_code: data.cityCode,
                    flat_number: data.flatNumber,
                    house_number: data.houseNumber,
                    name: data.name,
                    pesel: data.pesel,
                    street: data.street,
                    surname: data.surname
                }),
            });
            const resp = await response.json();

            alert(resp.message);


        } catch (e) {
            alert("Connection lost");
        }
    };

    const handleClose = () => {
        ctx.checkToken()
        setOpen(false);
        setUserChange(JSON.parse(JSON.stringify(user)))
    };

    function handleConfirm() {
        ctx.checkToken()
        editPerson(userChange).then(()=>{
            setOpen(false);
        })

    }

    function handleChangeName(event) {
        userChange.name = event.target.value
    }

    function handleChangeSurname(event) {
        userChange.surname = event.target.value
    }

    return(
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Profile</DialogTitle>
            <DialogContent>

                <TextField
                    autoFocus
                    margin="dense"
                    id="name"
                    label="Name"
                    fullWidth
                    defaultValue={user.name}
                    variant="standard"
                    onChange={handleChangeName}
                />

                <TextField
                    autoFocus
                    margin="dense"
                    id="name"
                    label="Surname"
                    fullWidth
                    defaultValue={user.surname}
                    variant="standard"
                    onChange={handleChangeSurname}
                />

                <TextField
                    autoFocus
                    margin="dense"
                    id="city"
                    label="City"
                    fullWidth
                    defaultValue={user.city}
                    variant="standard"
                    onChange={(event)=>{
                        userChange.city = event.target.value
                    }}
                />

                <TextField
                    autoFocus
                    margin="dense"
                    id="kod"
                    label="Kod Pocztowy"
                    fullWidth
                    defaultValue={user.cityCode}
                    variant="standard"
                    onChange={(event)=>{
                        userChange.cityCode = event.target.value
                    }}
                />

                <TextField
                    autoFocus
                    margin="dense"
                    id="street"
                    label="Ulica"
                    fullWidth
                    defaultValue={user.street}
                    variant="standard"
                    onChange={(event)=>{
                        userChange.street = event.target.value
                    }}
                />

                <TextField
                    autoFocus
                    margin="dense"
                    id="house"
                    label="Numer Domu"
                    fullWidth
                    defaultValue={user.houseNumber}
                    variant="standard"
                    onChange={(event)=>{
                        userChange.houseNumber = event.target.value
                    }}
                />

                <TextField
                    autoFocus
                    margin="dense"
                    id="flat"
                    label="Numer Mieszkania"
                    fullWidth
                    defaultValue={user.flatNumber}
                    variant="standard"
                    onChange={(event)=>{
                        userChange.flatNumber = event.target.value
                    }}
                />

            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Anuluj</Button>
                <Button onClick={handleConfirm}>Zapisz</Button>
            </DialogActions>
        </Dialog>
    );
}