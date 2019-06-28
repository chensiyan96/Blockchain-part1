package main

import (
	"encoding/json"
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type SmartContract struct{}

// Data type
type Record struct {
	EncrypedMessage string `json:"encryped_message"`
}
type UserInfo struct {
	EncrypedMessage string `json:"encryped_message"`
}

// End date type

// End User defined chain code struct

// User defined smart contract
func (s *SmartContract) Init(APIstub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)
}

func (s *SmartContract) Invoke(APIstub shim.ChaincodeStubInterface) pb.Response {
	function, args := APIstub.GetFunctionAndParameters()

	if function == "insertUserInfo" {
		return s.insert_user_info(APIstub, args)
	} else if function == "queryUserInfo" {
		return s.query_user_info(APIstub, args)
	} else if function == "queryContractRecord" {
		return s.query_contract_record(APIstub, args)
	} else if function == "queryFinancingApplyRecord" {
		return s.query_financing_apply_record(APIstub, args)
	} else if function == "queryTransactionRecord" {
		return s.query_transaction_record(APIstub, args)
	} else if function == "insertContractRecord" {
		return s.insert_contract_record(APIstub, args)
	} else if function == "insertFinancingApplyRecord" {
		return s.insert_financing_apply_record(APIstub, args)
	} else if function == "insertTransactionRecord" {
		return s.insert_transaction_record(APIstub, args)
	} else if function == "initedger" {
		return s.initLedger(APIstub)
	} else {
		return shim.Error("Invalid smart contract function name!")
	}
}

func (s *SmartContract) initLedger(APIstub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)
}

func get_user_info_key(username string) string {
	return "<username>" + username
}

func get_contract_key(record_id string) string {
	// return "<record_key>" + strconv.FormatInt(record_id, 10)
	return "<contract_key>" + record_id
}

func get_financing_apply_key(record_id string) string {
	return "<financing_apply_key>" + record_id
}

func get_transaction_key(record_id string) string {
	return "<transaction_key>" + record_id
}

func (t *SmartContract) insert_user_info(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// args[0]: username
	// args[1]: message
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}
	var user_info = UserInfo{
		EncrypedMessage: args[1]}
	fmt.Println(user_info)
	user_info_as_bytes, other_info := json.Marshal(user_info)
	fmt.Println(other_info)
	fmt.Println(user_info_as_bytes)
	err := stub.PutState(get_user_info_key(args[0]), user_info_as_bytes)
	fmt.Println(err)
	return shim.Success(nil)
}

func (t *SmartContract) query_user_info(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// args[0]: username
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Excepting 1")
	}
	user_info_as_bytes, _ := stub.GetState(get_user_info_key(args[0]))
	return shim.Success(user_info_as_bytes)
}

func (t *SmartContract) insert_contract_record(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// args[0]: record_id
	// args[1]: message
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}
	var record = Record{
		EncrypedMessage: args[1]}
	fmt.Println(record)
	user_info_as_bytes, other_info := json.Marshal(record)
	fmt.Println(other_info)
	fmt.Println(user_info_as_bytes)
	err := stub.PutState(get_contract_key(args[0]), user_info_as_bytes)
	fmt.Println(err)
	return shim.Success(nil)
}

func (t *SmartContract) insert_financing_apply_record(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// args[0]: record_id
	// args[1]: message
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}
	var record = Record{
		EncrypedMessage: args[1]}
	fmt.Println(record)
	user_info_as_bytes, other_info := json.Marshal(record)
	fmt.Println(other_info)
	fmt.Println(user_info_as_bytes)
	err := stub.PutState(get_financing_apply_key(args[0]), user_info_as_bytes)
	fmt.Println(err)
	return shim.Success(nil)
}

func (t *SmartContract) insert_transaction_record(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// args[0]: record_id
	// args[1]: message
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}
	var record = Record{
		EncrypedMessage: args[1]}
	fmt.Println(record)
	user_info_as_bytes, other_info := json.Marshal(record)
	fmt.Println(other_info)
	fmt.Println(user_info_as_bytes)
	err := stub.PutState(get_transaction_key(args[0]), user_info_as_bytes)
	fmt.Println(err)
	return shim.Success(nil)
}

func (t *SmartContract) query_contract_record(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// args[0]: record_id
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Excepting 1")
	}
	record_as_bytes, _ := stub.GetState(get_contract_key(args[0]))
	return shim.Success(record_as_bytes)
}

func (t *SmartContract) query_financing_apply_record(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// args[0]: record_id
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Excepting 1")
	}
	record_as_bytes, _ := stub.GetState(get_financing_apply_key(args[0]))
	return shim.Success(record_as_bytes)
}

func (t *SmartContract) query_transaction_record(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// args[0]: record_id
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Excepting 1")
	}
	record_as_bytes, _ := stub.GetState(get_transaction_key(args[0]))
	return shim.Success(record_as_bytes)
}

// End user defined smart contrct

func main() {
	err := shim.Start(new(SmartContract))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}