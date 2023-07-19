import Box from "@mui/material/Box";

import {useCallback, useContext, useEffect, useState} from "react";
import Typography from "@mui/material/Typography";
import * as React from "react";
import CssBaseline from "@mui/material/CssBaseline";
import NavBar from "../components/NavBar";
import Container from "@mui/material/Container";
import {appConfig} from "../config";
import Context from "../context/context";
import {createSearchParams, useNavigate} from "react-router-dom";
import SearchBar from "../components/SearchBar";

export default function Find_Person() {

    const [searchInput, setSearchInput] = useState('')
    const [pesels, setPesels] = useState([])
    const ctx = useContext(Context)
    const navigate = useNavigate()

    const getSearch = useCallback(() => {
        const xHttp = new XMLHttpRequest();
        let json;
        let obj;
        xHttp.onreadystatechange = function () {

            if (this.readyState === 4 && this.status === 200) {
                json = xHttp.responseText;

                obj = JSON.parse(json);
                setPesels(obj);

            }
            if (this.readyState === 4 && this.status === 400) {
                console.log("No access.");
            }
        };

        xHttp.open(
            "GET",
            `${appConfig.server}/person/getPeselAutocomplete?pesel=${searchInput}&page=1`,
            true,
            null,
            null
        );
        xHttp.setRequestHeader('Authorization', 'Bearer ' + ctx.authToken)
        xHttp.send();

    }, [ctx.authToken, searchInput]);

    useEffect(() => {
        if(searchInput !== '') {
            getSearch();
        }
    }, [getSearch, searchInput]);

    function handleSearchPerson() {
        ctx.checkToken()
        if(pesels.length > 0) {
            navigate(`/person/${searchInput}`)
        }
    }

    return (

        <Box>
            <NavBar/>


            <Container className="blackout">

                <SearchBar
                    getSearch={getSearch}
                    searchInput={searchInput}
                    setSearchInput={setSearchInput}
                    data={pesels}
                    setData={setPesels}
                    handleSearchPerson={handleSearchPerson}/>

            </Container>

        </Box>
    )
}