import * as React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import MenuItem from '@mui/material/MenuItem';


const accountTypes = [
    {
        value: 'ROLE_USER',
        label: 'Obywatel',
    },
    {
        value: 'ROLE_ADMIN',
        label: 'Pracownik',
    },

];

export default function SelectAccountType({value, setValue}) {

    function handleChangeValue(event){
        setValue(event.target.value)
    }

    return (
        <Box
            sx={{
                '& .MuiTextField-root': { mt: 2},
            }}
            noValidate
            component="form"
            autoComplete="off">

            <div>
                <TextField
                    id="outlined-select-currency"
                    value = {value}
                    select
                    fullWidth
                    onChange={handleChangeValue}
                    label="Typ Konta"
                    defaultValue="Obywatel">
                    
                    {accountTypes.map((option) => (
                        <MenuItem key={option.value} value={option.value}>
                            {option.label}
                        </MenuItem>
                    ))}
                </TextField>

            </div>
        </Box>
    );
}