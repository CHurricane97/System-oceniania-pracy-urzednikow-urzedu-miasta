import React, {useCallback, useState} from "react";

import {useNavigate} from "react-router-dom";
import {appConfig} from "../config";

const Context = React.createContext({
    authToken: null,
    isLoggedIn: false,
});

export const ContextProvider = (props) => {

    const [authToken, setAuthToken] = useState(localStorage.getItem('token'));
    const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));
    const navigate = useNavigate();

    const checkToken = useCallback(() => {
        const xHttp = new XMLHttpRequest();
        xHttp.onreadystatechange = function () {
            if (this.readyState === 4 && this.status !== 200) {
                logout()
            }
        };

        xHttp.open(
            "GET",
            `${appConfig.server}/test/admin`,
            true,
            null,
            null
        );
        xHttp.setRequestHeader('Authorization', 'Bearer ' + authToken)
        xHttp.send();

    }, [authToken]);

    const logout = ()=>{
        setIsLoggedIn(false)
        setAuthToken(null);
        localStorage.removeItem('token');
    }

    const login = async (data) => {
        const url = `${appConfig.server}/auth/loginAdmin`;
        try {
            const response = await fetch(`${url}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    login: data.login,
                    password: data.password,
                }),
            });
            const resp = await response.json();
            if (response.ok) {
                setIsLoggedIn(true)
                setAuthToken(resp.token);
                localStorage.setItem('token', resp.token);
                navigate("/menuWorker");
            } else {
                alert(resp.message);
            }
        } catch (e) {
            alert("Connection lost");
        }
    };

    return (
        <Context.Provider
            value={{
                authToken,
                isLoggedIn,
                login,
                logout,
                checkToken,
            }}
        >
            {props.children}
        </Context.Provider>
    );
};

export default Context;