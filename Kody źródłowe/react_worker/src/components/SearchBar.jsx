import {useEffect} from "react";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";
import {Autocomplete} from "@mui/material";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import SearchIcon from "@mui/icons-material/Search";
import * as React from "react";

const AutocompleteStyle = {
    width: "100%",
    display: "flex",
};

const GridStyle = {
    flexDirection: "row",
    alignItems: "center",
    flexWrap: "nowrap",
    marginBottom: 10
}

const SearchButtonStyle = {
    marginLeft: 2,
};

const IconStyle = {
    margin: 1
}

export default function SearchBar({getSearch, handleSearchPerson, data, setData, searchInput, setSearchInput}) {

    useEffect(() => {
        if (searchInput !== '') {
            getSearch();
        }
    }, [getSearch, searchInput]);

    return (
        <Box>

            <Typography className="test" component="h1" variant="h4"
                        sx={{
                            marginTop: 4,
                            marginBottom: 10,
                            textAlign: "center",
                        }}>
                Wyszukaj Osobę:
            </Typography>

            <form onSubmit={handleSearchPerson}>
                <Grid container sx={GridStyle}>
                    <Autocomplete
                        freeSolo
                        sx={AutocompleteStyle}
                        onInputChange={(e, v) => {
                            setData([])
                            setSearchInput(v)
                        }}
                        inputValue={searchInput}
                        options={data.map((pesel) => pesel)}
                        renderInput={(params) =>
                            <TextField {...params} label="Pesel"/>}/>

                    <Button sx={SearchButtonStyle} type="submit">
                        <SearchIcon sx={IconStyle}/>
                    </Button>
                </Grid>
            </form>

        </Box>
    )
}